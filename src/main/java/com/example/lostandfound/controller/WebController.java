package com.example.lostandfound.controller;

import com.example.lostandfound.model.Item;
import com.example.lostandfound.model.Claim;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.repository.ClaimRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class WebController {

    private final ItemRepository itemRepository;
    private final ClaimRepository claimRepository;

    public WebController(ItemRepository itemRepository, ClaimRepository claimRepository) {
        this.itemRepository = itemRepository;
        this.claimRepository = claimRepository;
    }

    @ModelAttribute("currentUser")
    public String currentUser(HttpSession session) {
        return (String) session.getAttribute("loggedInUser");
    }

    @GetMapping("/")
    public String index(@RequestParam(value="q", required=false) String query,
                        @RequestParam(value="filter", required=false) String filter, 
                        Model model) {
        
        List<Item> items;
        Item.ItemType itemType = null;
        if(filter != null && !filter.isEmpty() && !filter.equalsIgnoreCase("ALL")) {
            try {
                itemType = Item.ItemType.valueOf(filter.toUpperCase());
            } catch(IllegalArgumentException e) {}
        }

        if (query != null && !query.trim().isEmpty()) {
            if (itemType != null) {
                items = itemRepository.searchByTypeAndKeyword(itemType, query);
            } else {
                items = itemRepository.searchByKeyword(query);
            }
            model.addAttribute("searchQuery", query);
        } else {
            if (itemType != null) {
                items = itemRepository.findByTypeOrderByCreatedAtDesc(itemType);
            } else {
                items = itemRepository.findAllByOrderByCreatedAtDesc();
            }
        }
        
        model.addAttribute("recentItems", items);
        model.addAttribute("currentFilter", filter == null ? "ALL" : filter);
        return "index";
    }

    @GetMapping("/lost")
    public String viewLost(Model model) {
        model.addAttribute("items", itemRepository.findByTypeOrderByCreatedAtDesc(Item.ItemType.LOST));
        model.addAttribute("viewType", "Lost Items");
        return "list";
    }

    @GetMapping("/found")
    public String viewFound(Model model) {
        model.addAttribute("items", itemRepository.findByTypeOrderByCreatedAtDesc(Item.ItemType.FOUND));
        model.addAttribute("viewType", "Found Items");
        return "list";
    }

    @GetMapping("/report")
    public String reportForm(@RequestParam(value = "type", required = false) Item.ItemType type, Model model) {
        Item item = new Item();
        if(type != null) {
            item.setType(type);
        }
        model.addAttribute("item", item);
        return "report";
    }

    @PostMapping("/report")
    public String submitReport(@ModelAttribute Item item, RedirectAttributes redirectAttributes, jakarta.servlet.http.HttpSession session) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username != null) {
            item.setPostedByUsername(username);
        }
        itemRepository.save(item);
        redirectAttributes.addFlashAttribute("successMessage", "Item successfully reported!");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        if ("password".equals(password)) {
            session.setAttribute("loggedInUser", username);
            redirectAttributes.addFlashAttribute("successMessage", "Welcome, " + username + "!");
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid credentials! Use any username and 'password'.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been logged out.");
        return "redirect:/";
    }

    public static class ItemWithClaims {
        private Item item;
        private List<Claim> claims;
        public ItemWithClaims(Item item, List<Claim> claims) { this.item = item; this.claims = claims; }
        public Item getItem() { return item; }
        public List<Claim> getClaims() { return claims; }
    }

    @GetMapping("/claim/{id}")
    public String claimForm(@PathVariable String id, Model model, jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to claim an item.");
            return "redirect:/login";
        }
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null || item.getType() != Item.ItemType.FOUND || !"OPEN".equals(item.getItemStatus())) {
            return "redirect:/";
        }
        model.addAttribute("item", item);
        return "claim-form";
    }

    @PostMapping("/claim/{id}")
    public String submitClaim(@PathVariable String id, @ModelAttribute Claim claim, jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return "redirect:/login";
        }
        claim.setItemId(id);
        claim.setClaimantUsername(username);
        claim.setStatus(Claim.ClaimStatus.PENDING);
        claimRepository.save(claim);
        redirectAttributes.addFlashAttribute("successMessage", "Your claim has been submitted!");
        return "redirect:/";
    }

    @GetMapping("/my-items")
    public String myItems(Model model, jakarta.servlet.http.HttpSession session) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return "redirect:/login";
        }
        List<Item> allItems = itemRepository.findAll();
        List<ItemWithClaims> itemsWithClaims = allItems.stream()
                .filter(i -> username.equals(i.getPostedByUsername()))
                .map(i -> new ItemWithClaims(i, claimRepository.findByItemId(i.getId())))
                .toList();
        model.addAttribute("itemsWithClaims", itemsWithClaims);
        return "my-items";
    }

    @PostMapping("/claim/{claimId}/approve")
    public String approveClaim(@PathVariable String claimId, jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        Claim claim = claimRepository.findById(claimId).orElse(null);
        if (claim != null) {
            claim.setStatus(Claim.ClaimStatus.APPROVED);
            claimRepository.save(claim);
            Item item = itemRepository.findById(claim.getItemId()).orElse(null);
            if(item != null) {
                item.setItemStatus("CLAIMED");
                itemRepository.save(item);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Claim approved!");
        }
        return "redirect:/my-items";
    }

    @PostMapping("/claim/{claimId}/reject")
    public String rejectClaim(@PathVariable String claimId, jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        Claim claim = claimRepository.findById(claimId).orElse(null);
        if (claim != null) {
            claim.setStatus(Claim.ClaimStatus.REJECTED);
            claimRepository.save(claim);
            redirectAttributes.addFlashAttribute("successMessage", "Claim rejected.");
        }
        return "redirect:/my-items";
    }
}
