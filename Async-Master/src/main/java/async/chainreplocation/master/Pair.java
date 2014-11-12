package async.chainreplocation.master;

class Pair<F,S> {
	F first;
	S second;
	
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public F getFirst() {
		return first;
	}
	
	public S getSecond() {
		return second;
	}
	
	public void setFirst(F first) {
		this.first = first;
	}
	
	public void setSecond(S second) {
		this.second = second;
	}
	
}