import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MapStrc {

	static <K, V> void show(Map<K, V> map) throws Exception {
		Field f = map.getClass().getDeclaredField("loadFactor");
		f.setAccessible(true);
		float lf = f.getFloat(map);

		f = map.getClass().getDeclaredField("threshold");
		f.setAccessible(true);
		int th = f.getInt(map);

		f = map.getClass().getDeclaredField("size");
		f.setAccessible(true);
		int size = f.getInt(map);

		int bkt=0, // #null buckets
				tblLength=0; 
		f = map.getClass().getDeclaredField("table");
		f.setAccessible(true);
		Object[] tbl =  (Object[])f.get(map);
		if (tbl!=null) {
			tblLength = tbl.length;
			for (Object o:tbl)
				if (o==null)
					bkt++;
		}

		System.out.println("loadFactor = "+lf+"\nthreshold = "+th
				+"\ntotal #buckets = capacity =  "+tblLength  
				+"\ntotal #elements =  "+size
				+"\n\t#buckets used = "+(tblLength-bkt)
				+"\n\t#collisions="+(size-tblLength+bkt));
		System.out.println("==================================");
	}

	public static void main(String[] args) throws Exception {
//		Map<Integer, Integer> map = new HashMap<>();
//		show(map);
//
//		for (int i=0; i<10; i++)
//			map.put(i, 10*i);
//
//		show(map);
//		
//		map.put(10, 10);
//		map.put(11, 10);
//		show(map);
//
//		map.put(12, 10);
//		show(map);
//		
//		map.clear();
//		show(map);
		
		resizes();
	}

	static final int CAPACITY = (int)Math.pow(2, 24);  // 16_777_216 
	static 	int entryCnt = CAPACITY*3/4;
	static void resizes() throws Exception{
		long startIn=0, endIn=55;
		long total=0;
		int mark = entryCnt>>1;
		Map<Integer, Integer> map = new HashMap<>(CAPACITY);
		for (int i=0; i<entryCnt; i++) 
			if (i>=mark) {
				if (i==mark) {
					System.out.println("BEFORE:"+"\n"
									 + "======");
					show(map);
				}
				startIn = System.nanoTime();
				map.put(i, i);
				endIn = System.nanoTime();
				if (i==mark) {
					System.out.println("\nlinear put() "+(endIn-startIn)/1000000+"\n");
					System.out.println("AFTER:"+"\n"
							 		 + "=====");
					show(map);
				} else total+=endIn-startIn;
			} else map.put(i, i);
		System.out.println("\n---> TOTAL -- "+total/1000000);
		System.out.println("\tavg put() -- "+(double)(total/(entryCnt-mark-1)/1000000));
		System.out.println("\nFINAL:"+"\n"
		 		 + "=====");
		show(map);

	}
}
