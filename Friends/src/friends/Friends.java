package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();
		String[] prev = new String[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(int x=0;x<g.members.length;x++)
			map.put(g.members[x].name, x);
		
		prev[map.get(p1)]=g.members[map.get(p1)].name;
		visited[map.get(p1)]=true;
		
		Queue<Person> q = new Queue<Person>();
		q.enqueue(g.members[map.get(p1)]);
		
		//bfs from p1
		while(!q.isEmpty()&&!visited[map.get(p2)]) {
			
			Person p = q.dequeue();
			Friend ptr = p.first;
			while(ptr!=null) {
				if(!visited[ptr.fnum]) {
					q.enqueue(g.members[ptr.fnum]);
					visited[ptr.fnum]=true;
					prev[ptr.fnum] = p.name;
				}
				ptr=ptr.next;
			}
			
		}
		
		int end = map.get(p2);
		
		//if bfs does not find p2
		if(!visited[end])
			return null;
		
		ArrayList<String> path = new ArrayList<String>();
		path.add(0,g.members[end].name);	
		while(!g.members[end].name.equals(prev[end])){
			end = map.get(prev[end]);
			path.add(0,g.members[end].name);	
		}
		
		return path;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		school = school.toLowerCase();
		
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		for(int x=0;x<g.members.length;x++) {
			
			if(school.equals(g.members[x].school)) {
				
				boolean isInList = false;
				for(ArrayList<String> subList : list) {
					for(String name : subList) {
						if(name.equals(g.members[x].name))
							isInList = true;
					}
				}
				
				if(!isInList) {
					
					ArrayList<String> c = new ArrayList<String>();
					
					//c.add(g.members[x].name);
					boolean[] visited = new boolean[g.members.length];
					HashMap<String,Integer> map = new HashMap<String,Integer>();
					for(int i=0;i<g.members.length;i++) {
						map.put(g.members[i].name, i);
					}
					
					Queue<Person> q = new Queue<Person>();
					q.enqueue(g.members[x]);
					while(!q.isEmpty()) {
						
						Person p = q.dequeue();
						c.add(p.name);
						visited[map.get(p.name)]=true;
						Friend ptr = p.first;
						while(ptr!=null) {
							if(!visited[ptr.fnum]&&
								school.equals(g.members[ptr.fnum].school)) {
									q.enqueue(g.members[ptr.fnum]);
							}
							ptr=ptr.next;
						}
						
					}
				list.add(c);	
					
				}
				
			}
			
		}
		if(list.size()==0)
			return null;
		
		return list;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		ArrayList<String> list = new ArrayList<String>();
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(int i=0;i<g.members.length;i++) {
			map.put(g.members[i].name, i);
		}
		
		//start person index
		HashSet<String> people = new HashSet<String>();
		for(int y=0;y<g.members.length;y++) {
			dfsnum = new int[g.members.length];
			back = new int[g.members.length];
			visited = new boolean[g.members.length];
			dfs(g.members[y],list,g,0,dfsnum,back,visited,map);
			if(list.size()>0)
				list.remove(list.size()-1);
			for(int x=0;x<list.size();x++) {
				people.add(list.get(x));
			}
			list.clear();
		}
		
		Iterator<String> it = people.iterator();
		while(it.hasNext()){
			list.add((String)it.next());
		}
		if(list.size()==0)
			return null;
		
		return list;
		
	}
	
	private static int dfs(Person p, ArrayList<String> list, Graph g, int count,
			int[] dfsnum, int[] back, boolean[] visited, HashMap<String,Integer> map) {
		count++;
		visited[map.get(p.name)]=true;
		//start VISIT
		dfsnum[map.get(p.name)]=count;
		back[map.get(p.name)]=count;
		//end VISIT
		//System.out.println(p.name+" "+count);
		
		Friend ptr = p.first;
		while(ptr!=null) {
			if(!visited[ptr.fnum]) {
				count=
				dfs(g.members[ptr.fnum],list,g,count,dfsnum,back,visited,map);
				//start BACKED UP
				if(dfsnum[map.get(p.name)]>back[ptr.fnum])
					back[map.get(p.name)]= 
						Math.min(back[map.get(p.name)], back[ptr.fnum]);
				else{
					list.add(p.name);
				}
				//end BACKED UP
			}else {
				//start ALREADY VISITED
				back[map.get(p.name)]=
						Math.min(back[map.get(p.name)], dfsnum[ptr.fnum]);
				//end ALREADY VISITED
			}
		ptr=ptr.next;
		}
		return count;
	}
	
}

