package com.maker.demo.recommend.recommender;

import com.maker.demo.recommend.analyzer.CollaborativeFilteringAnalyzer;
import com.maker.demo.recommend.bean.BasicBean;
import com.maker.demo.recommend.bean.HabitsBean;
import com.maker.demo.recommend.input.ReaderFormat;
import com.maker.demo.util.TXTUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * This is the core in the system,it's a recommend system based on user field.
 * It calculate results with the smiliarity for users' habits.
 * @author wqd
 *
 */
public class UserRecommender {
	
	/**
	 * The method recommend a lists of items for users after calculate the similarity and get the 
	 * top range.According to the most similar users, there is a items lists to recommend a user
	 * serval items. 
	 * @param lists
	 * @param recommendTable
	 * @param user
	 * @param range
	 * @return
	 */
	public List<String> recommend(List<HabitsBean> lists, float[][] recommendTable, BasicBean user, int range) {
		int i = 0;
		HabitsBean bean = null;
		for ( ; i < lists.size(); i++) {
			bean = lists.get(i);
			if(bean.getId() == user.getId())
				break;
		}
		// TODO throws over size exception
		
		//get the top recommend table value of range 
		List<Float> row = new ArrayList<Float>(recommendTable[i].length);
		for (int j = 0; j < recommendTable[i].length; j++) {
			row.add(recommendTable[i][j]);
		}
		Collections.sort(row);
		
		//get the common range of these HabitBean
		int start = row.size() - 1;
		Set<String> sets = new HashSet<String>();
		for (int j = start; j > start - range; j--) {
			// the lists id  aren't frequently.
			HabitsBean habits = lists.get(j);
			sets.addAll(habits.getArray());
		}
		
		sets.removeAll(bean.getArray());

		return new ArrayList<String>(sets);
	}
	public static List<String> getRec(int id) throws IOException {
		UserRecommender recommender = new UserRecommender();
		List<HabitsBean> lists = new ReaderFormat().formateLogUser(TXTUtils.txtFilePath);
		float[][] recommendTable = new CollaborativeFilteringAnalyzer().userSimilarityConsine(lists);
		HabitsBean user = new HabitsBean();
		user.setId(id);
		System.out.println(user.toString());
		//recommender.recommend(lists , recommendTable, user, 5);
		List<String> rec =  recommender.recommend(lists , recommendTable, user, 5);
		rec.removeAll(user.getArray());
		rec = rec.subList(0,9);
		/*HabitsBean check = new HabitsBean();
		for ( int i=0; i < lists.size(); i++) {
			user = lists.get(i);
			if(check.getId() == user.getId())
				break;
		}*/

		System.out.println(rec.toString());
		return rec;
	}

	public static void main(String[] args) throws IOException {
		getRec(1000011);
		//[1143, 1141, 1139, 1134, 1013, 1056, 1012, 1154, 1153]
		/*for (String str:rec) {
			System.out.println(str);
		}*/
	}
}
