package com.example.lostandfound;

import com.example.lostandfound.model.Item;
import com.example.lostandfound.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class LostAndFoundApplication {

	public static void main(String[] args) {
		SpringApplication.run(LostAndFoundApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(ItemRepository repository) {
		return args -> {
			if (repository.count() < 10) {
				Item item1 = new Item();
				item1.setTitle("Vintage Gold Pocket Watch");
				item1.setDescription("Found a beautiful antique pocket watch with a gold chain near the central fountain. The glass is slightly scratched.");
				item1.setLocation("Central Park Fountain");
				item1.setDateLostOrFound(LocalDate.now().minusDays(1));
				item1.setType(Item.ItemType.FOUND);
				item1.setContactName("Security Office");
				item1.setContactEmail("security@campus.edu");
				repository.save(item1);

				Item item2 = new Item();
				item2.setTitle("Brown Leather Wallet");
				item2.setDescription("Lost my brown leather wallet containing ID, cards, and some cash. Sentimental value attached.");
				item2.setLocation("Engineering Building II - Library");
				item2.setDateLostOrFound(LocalDate.now().minusDays(2));
				item2.setType(Item.ItemType.LOST);
				item2.setContactName("John Smith");
				item2.setContactEmail("john.smith@example.com");
				repository.save(item2);

				Item item3 = new Item();
				item3.setTitle("Set of 3 Keys on a Blue Lanyard");
				item3.setDescription("Found a set of keys attached to a blue 'NYU' lanyard. One building key and two smaller keys.");
				item3.setLocation("Cafeteria Table 4");
				item3.setDateLostOrFound(LocalDate.now().minusDays(3));
				item3.setType(Item.ItemType.FOUND);
				item3.setContactName("Cafeteria Staff");
				item3.setContactEmail("cafeteria-lost@example.edu");
				repository.save(item3);

				Item item4 = new Item();
				item4.setTitle("Black North Face Umbrella");
				item4.setDescription("I left my black folding umbrella in the lecture hall yesterday. Large size.");
				item4.setLocation("Lecture Hall 101");
				item4.setDateLostOrFound(LocalDate.now().minusDays(1));
				item4.setType(Item.ItemType.LOST);
				item4.setContactName("Emily Chen");
				item4.setContactEmail("echen.student@example.edu");
				repository.save(item4);

				Item item5 = new Item();
				item5.setTitle("Silver Macbook Pro 14\"");
				item5.setDescription("Found a silver Macbook Pro left on a desk. Brought it to the IT support desk.");
				item5.setLocation("Student Center Study Area");
				item5.setDateLostOrFound(LocalDate.now().minusDays(0));
				item5.setType(Item.ItemType.FOUND);
				item5.setContactName("IT Helpdesk");
				item5.setContactEmail("it-support@example.edu");
				repository.save(item5);

				Item item6 = new Item();
				item6.setTitle("Ray-Ban Aviator Sunglasses");
				item6.setDescription("Lost my sunglasses during the outdoor event. Gold frame, black lenses.");
				item6.setLocation("Campus Main Lawn");
				item6.setDateLostOrFound(LocalDate.now().minusDays(4));
				item6.setType(Item.ItemType.LOST);
				item6.setContactName("Michael R.");
				item6.setContactEmail("michael.r@example.com");
				repository.save(item6);

				Item item7 = new Item();
				item7.setTitle("Reading Glasses in Red Case");
				item7.setDescription("Found a pair of reading glasses inside a hard red velvet case.");
				item7.setLocation("Bus Stop outside Science Wing");
				item7.setDateLostOrFound(LocalDate.now().minusDays(2));
				item7.setType(Item.ItemType.FOUND);
				item7.setContactName("Transit Office");
				item7.setContactEmail("transit@example.edu");
				repository.save(item7);

				Item item8 = new Item();
				item8.setTitle("Sony Wireless Headphones");
				item8.setDescription("Lost my gray Sony WH-1000XM4 headphones. Very important for studying!");
				item8.setLocation("Silent Study Floor, Library");
				item8.setDateLostOrFound(LocalDate.now().minusDays(1));
				item8.setType(Item.ItemType.LOST);
				item8.setContactName("Sarah Jones");
				item8.setContactEmail("sarahj99@example.com");
				repository.save(item8);

				Item item9 = new Item();
				item9.setTitle("Green Moleskine Notebook");
				item9.setDescription("Found a notebook with sketches and chemistry notes. No name inside.");
				item9.setLocation("Chemistry Lab 304");
				item9.setDateLostOrFound(LocalDate.now().minusDays(5));
				item9.setType(Item.ItemType.FOUND);
				item9.setContactName("Lab Assistant Tom");
				item9.setContactEmail("tom.lab@example.edu");
				repository.save(item9);

				Item item10 = new Item();
				item10.setTitle("Silver Claddagh Ring");
				item10.setDescription("Lost my grandmother's ring near the restrooms. High emotional value.");
				item10.setLocation("Arts Building First Floor Restrooms");
				item10.setDateLostOrFound(LocalDate.now().minusDays(2));
				item10.setType(Item.ItemType.LOST);
				item10.setContactName("Jessica W.");
				item10.setContactEmail("jessica.w@example.com");
				repository.save(item10);
			}
		};
	}
}
