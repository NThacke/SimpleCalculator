public class Stack<T> {
	private DLL<T> myList;
	
	public Stack() {
		myList=new DLL<T>();
	}
	
	public void push(T element) {
		myList.addFirst(element);
	}
	
	public T pop() {
		T element=null;
		if (myList.size()>0) {
			element = myList.getFirst();
			myList.deleteFirst();
		}
		return element;
	}

	public T peek() {
		T element = null;
		element = this.pop();
		this.push(element);
		return element;
	}
	
	public boolean isEmpty() {
		return myList.size()==0;
	}
	
	public static void main(String[] args)
	{
		Stack<String> myStack= new Stack<String>();
		String expression="(((7)*(5+3)-8)/3+9)*3)";
		for (int i=0;i<expression.length();i++) {
			if (expression.charAt(i)=='(') {
				myStack.push("a");
			}
			else if (expression.charAt(i)==')') {
				if (!myStack.isEmpty()) {
					myStack.pop();
				}
				else {
					System.out.println("Mismatch");
					return;
				}
			}			
		}
		if (!myStack.isEmpty()) {
			System.out.println("Mismatch");			
		}
		System.out.println("Match");
	}
}