public class DLL<T> {
	private Node<T> first;
	private Node<T> last;
	private int count;

	public DLL() {
		this.first=null;
		this.last=null;
		count=0;
	}
	
	public T getFirst() {
		return first.getData();
	}
	
	public T getLast() {
		return last.getData();
	}
	
	public void addFirst(T data) {
		Node<T> n=new Node<T>(data,null,first);
		if (this.first!=null) {
			this.first.setPrev(n);
		}
		else {
			this.last=n;
		}
		this.first=n;
		count++;
	}
	
	public void addLast(T data) {
		Node<T> n=new Node<T>(data,last,null);
		if (this.last!=null) {
			this.last.setNext(n);
		}
		else {
			this.first=n;
		}
		this.last=n;
		count++;
	}

	public void deleteFirst() {
		if (this.first!=null) {
			Node<T> newFirst=this.first.getNext();
			this.first=newFirst;
			if (newFirst!=null) {
				newFirst.setPrev(null);
			}
			else {
				this.last=null;
			}
			count--;
		}
	}

	public void deleteLast() {
		if (this.last!=null) {
			Node<T> newLast=this.last.getPrev();
			this.last=newLast;
			if (newLast!=null) {
				newLast.setNext(null);
			}
			else {
				this.first=null;
			}
			count--;
		}
	}


	public void traverse() {
		Node<T> current=this.first;
		while (current!=null) {
			System.out.print(current.getData()+"    ");
			current=current.getNext();
		}
		System.out.println();
	}

	
	public int size() {
		return count;
	}
	
}