package site;

import java.util.ArrayList;
import java.util.TreeSet;

import data.SearchResult;
import data.ViewingObject;
import data.ViewingObjectHolder;

public class Driver {

	public static void main(String[] args) {
		ViewingObject temp = null;
		ArrayList<String> tags = new ArrayList<>();
		TreeSet<String> tree = new TreeSet<>();

		tags.add("alpha");  // 0
		tags.add("bravo");  // 1
		tags.add("charlie");// 2
		tags.add("delta"); 	// 3
		tags.add("echo"); 	// 4
		tags.add("foxtrot");// 5
		tags.add("golf"); 	// 6
		tags.add("hotel"); 	// 7
		tags.add("india"); 	// 8
		tags.add("juliett");// 9
		tags.add("kilo"); 	// 10
		tags.add("lima"); 	// 11
		tags.add("mike"); 	// 12
		tags.add("november");//13
		tags.add("oscar"); 	// 14
		tags.add("papa"); 	// 15
		tags.add("quebec"); // 16
		tree.addAll(tags);

		ViewingObjectHolder ds = new ViewingObjectHolder();

		for (int i = 0; i < 17; i++) {
			temp = new ViewingObject(i + ".txt", Integer.toString(i), tree.headSet(tags.get(i), true));
			System.out.println("Temp with " + i + " is :\n" + temp);
			ds.addObject(temp);
		}

		ArrayList<String> searches = new ArrayList<>();
		searches.add("papa");
		searches.add("quebec");
		searches.add("alpha");

		System.out.println("searches: " + searches.toString());

		for (SearchResult search : ds.getByTags(searches)) {
			System.out.println(search.toString());
		}
	}
}
