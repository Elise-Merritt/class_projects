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
		
		/** COMPLETE THIS METHOD **/		
		HashMap<String, Integer> visited=new HashMap<String, Integer>();
		HashMap<String, String> previous= new HashMap<String, String>();
		ArrayList<String> answer = new ArrayList<String>();
		Queue q = new Queue();
		Person p=g.members[g.map.get(p1)];
		q.enqueue(p);
		int vNum=0;
		visited.put(p.name, vNum);
		while(!q.isEmpty()) {
			p=(Person) q.dequeue();		
			if(p.name.equals(p2)) {
				break;
			}else {
				Friend f=p.first;
				while(f!=null) {
					if(!visited.containsKey(g.members[f.fnum].name) && f!=null) {
					//	System.out.println(f.fnum);
						q.enqueue(g.members[f.fnum]);
						visited.put(g.members[f.fnum].name,vNum++);
						vNum=vNum+1;
						previous.put(g.members[f.fnum].name, p.name);
					}
					f=f.next;
				}
			}
			
	}
		if(!p.name.equals(p2)){
			return null;
		}
		String pointer=p2;
		while(pointer!=null) {
			if(previous.get(pointer)!=null) {
			answer.add(previous.get(pointer));
			}
			pointer=previous.get(pointer);
	//		System.out.println(pointer);
		}
		ArrayList<String> answer2=new ArrayList<String>();
		for(int i=answer.size()-1; i>=0; i--) {
			answer2.add(answer.get(i));
		}
		answer2.add(p2);
		return answer2;
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
		
		/** COMPLETE THIS METHOD **/
		ArrayList<ArrayList<String>> answer = new ArrayList<ArrayList<String>>();
		ArrayList<String> currClique = new ArrayList<String>();
		HashMap<String, Integer> visited = new HashMap<String, Integer>();
		Person s=null;
		for(int i=0; i<g.members.length; i++) {
			if(!(g.members[i].school==null) && g.members[i].school.equals(school)) {
				s=g.members[i];
				break;
			}
		}
		int x=0;
		Queue q=new Queue();
		q.enqueue(s);
		currClique.add(s.name);
		answer.add(currClique);
		visited.put(s.name, x);
		x++;
		while(!q.isEmpty()) {
			s=(Person) q.dequeue();
			Friend f=s.first;
			while(f!=null) {
				if(!visited.containsKey(g.members[f.fnum].name)) {
						if(!(g.members[f.fnum].school==null) && g.members[f.fnum].school.equals(school) ) {
							currClique.add(g.members[f.fnum].name);
							if(!answer.contains(currClique) && !(currClique.isEmpty())) {
								answer.add(currClique);
							}
							q.enqueue(g.members[f.fnum]);
						}else {
							currClique=new ArrayList<String>();
						}
						q.enqueue(g.members[f.fnum]);
						visited.put(g.members[f.fnum].name, x);
						x++;
				}
				f=f.next;
			}
		}

			if(!answer.contains(currClique) && !(currClique.isEmpty())) {
			answer.add(currClique);
		}
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
		if(answer.isEmpty()) {
			return null;
		}else {
			return answer;
		}
	}
	
	public static ArrayList<String> dfs(Person start, HashMap<String, Integer> vists, Graph gph, HashMap<String, Integer> bcks, int dfsnum, HashMap<String, Integer> dnums){
		ArrayList<String> a = new ArrayList<String>();
		ArrayList<String> conn = new ArrayList<String>();
		int n = dfsnum;
		a.add(start.name);
		vists.put(start.name, n);
		dnums.put(start.name, dfsnum);
		bcks.put(start.name, n);
		dfsnum++;
		n++;
		Friend xyz=start.first;
		Friend prev=xyz, prev2=xyz;
		int count=0;
		System.out.println("Start="+start.name);
		while(xyz!=null) {
			if(!vists.containsKey(gph.members[xyz.fnum].name)) {
				vists.put(gph.members[xyz.fnum].name, n);
				n++;
				dnums.put(start.name, dfsnum);
				bcks.put(gph.members[xyz.fnum].name, dfsnum);
				a.addAll(dfs(gph.members[xyz.fnum], vists, gph, bcks, n, dnums));
				dfsnum+=a.size()+1;
				//xyz=xyz.next;
			}
			prev2=prev;
			prev=xyz;
			xyz=xyz.next;
			count++;
		}
			Person r=gph.members[prev.fnum];
			Person neighbor=r;
			if(vists.containsKey(r.name)) {
				bcks.put(gph.members[prev2.fnum].name, Math.min(dnums.get(gph.members[prev2.fnum].name),bcks.get(gph.members[prev.fnum].name) ));
			}
			if(vists.get(gph.members[prev2.fnum].name)>bcks.get(gph.members[prev.fnum].name)) {
				bcks.put(gph.members[prev2.fnum].name, Math.min(bcks.get(gph.members[prev2.fnum].name),bcks.get(gph.members[prev.fnum].name) ));
			}else {
				if(!conn.contains(gph.members[prev2.fnum].name)) {
				conn.add(gph.members[prev2.fnum].name);
				}else {
					
				}
			}
		return conn;
	}
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> answer=new ArrayList<String>();
		/** COMPLETE THIS METHOD **/
		HashMap<String, Integer> visited=new HashMap<String, Integer>();
		HashMap<String, Integer> dfnums= new HashMap<String, Integer>();
		HashMap<String, Integer> backs=new HashMap<String, Integer>();
		int y=0;
	//	answer=dfs(g.members[0], visited, g, backs);
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		for(int i=0; i<g.members.length; i++) {
			if(!visited.containsKey(g.members[i].name)) {
				answer.addAll(dfs(g.members[i], visited, g, backs, y, dfnums));
				y+=answer.size()+1;
			}
		}
		if(answer.isEmpty()) {
			return null;
		}else {
		return answer;
		}
	}
}

