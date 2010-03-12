package util;

public class OcttreeNode 
{
	private Object data = null;
	private OcttreeNode[] children = new OcttreeNode[8]; //a list of its children
	
	public OcttreeNode(Object data, OcttreeNode[] children)
	{
		this.data = data;
		this.children = children;
	}
	
	public void setData(Object obj)
	{
		data = obj;
	}
	
	public Object getData()
	{
		return data;
	}
	
	public void setNthChild(int n, OcttreeNode o)
	{
		children[n] = o;
	}
	
	public OcttreeNode getNthChild(int n)
	{
		return children[n];
	}
	
	public Boolean isLeaf()
	{
		if(children.length == 0) return true;
		else return false;
	}
}
