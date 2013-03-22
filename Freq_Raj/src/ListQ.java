public class ListQ {

  public static class ListNode {

     public int value;

     public ListNode next;
  }
 
    public static boolean hasLoops(ListNode myList) {
            ListNode fastNode = myList;
            ListNode slowNode = myList;
            boolean hasLoops = false;
            while (fastNode != null && !hasLoops) {
                    slowNode = slowNode.next;
                    ListNode temp = fastNode.next;
                    fastNode = temp != null ? temp.next : temp;
                    if (fastNode == slowNode) {
                            hasLoops = true;
                    }
            }
            return hasLoops;
    }
    
    public static void main(String args[]){
    	ListNode node = new ListNode();
    	node.value = 10;
    	ListNode node1 = new ListNode();
    	node1.value = 10;
    	ListNode node2 = new ListNode();
    	node2.value = 20;    	    	    	
    	ListNode node3 = new ListNode();
    	node3.value = 20;
    	ListNode node4 = new ListNode();
    	node4.value = 20;
    	ListNode node5 = new ListNode();
    	node5.value = 20;
    	ListNode node6 = new ListNode();
    	node6.value = 20;
    	
    	node.next = node1;
    	node1.next = node2;
    	node2.next = node3;
    	node3.next = node4;
    	node4.next = node2;
    	node5.next = node6;
    	
    	System.out.println(hasLoops(node));
    }
}