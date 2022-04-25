import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Scanner;

class node
{
	int x,y;
	node child1,child2,child3;
	node parent;
	node(int x,int y,node p)
	{
		this.x=x;
		this.y=y;
		child1=null;
		child2=null;
		child3=null;
		parent=p;
		if(p!=null)
		{
			if(p.child1==null)
				p.child1=this;
			else if(p.child2==null)
				p.child2=this;
			else
				p.child3=this;
		}
	}
}

class listnode
{
	node data;
	listnode previous, next;
	listnode(node d, listnode l)
	{
		data=d;
		previous=null;
		next=l;
	}
}

public class MazeSolver
{
	node tree1,tree2,target1,target2;
	listnode leaves1,leaves2;
	BufferedImage im;
	long stime;
	int sx,sy,ex,ey;

	void init()
	{
		try
		{
			im = ImageIO.read(new File("maze.bmp"));
			Scanner sc=new Scanner(new File("maze.txt"));
			sx=sc.nextInt();
			sy=sc.nextInt();
			tree1 = new node(sx,sy,null);
			leaves1 = new listnode(tree1,null);
			ex=sc.nextInt();
			ey=sc.nextInt();
			tree2 = new node(ex,ey,null);
			leaves2 = new listnode(tree2,null);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public void solve()
	{
		try
		{
			listnode p=null,q=null;
			node t=null,c=null;
			boolean up=false,down=false,left=false,right=false;
			int d=0;
			stime = System.currentTimeMillis();
			while(true)
			{
			//	System.out.println("\nActive leaves of forward tree");
				for(p=leaves1;p!=null;p=p.next)
				{
					t=p.data;
				//	System.out.print("("+t.x+","+t.y+") ");
					if((im.getRGB(t.x,t.y)&0xffffff)==0xfffffd)
					{
						for(q=leaves2;q!=null;q=q.next)
							if(t.x==q.data.x&&Math.abs(t.y-q.data.y)==1||t.y==q.data.y&&Math.abs(t.x-q.data.x)==1)
							{
								System.out.println("Path found!");
								target1=t;
								target2=q.data;
								return;
							}
					}
					im.setRGB(t.x,t.y,0xfffffe);
					up = (im.getRGB(t.x,t.y-1)&0xffffff)==0xffffff||(im.getRGB(t.x,t.y-1)&0xffffff)==0xfffffd;
					down = (im.getRGB(t.x,t.y+1)&0xffffff)==0xffffff||(im.getRGB(t.x,t.y+1)&0xffffff)==0xfffffd;
					left = (im.getRGB(t.x-1,t.y)&0xffffff)==0xffffff||(im.getRGB(t.x-1,t.y)&0xffffff)==0xfffffd;
					right = (im.getRGB(t.x+1,t.y)&0xffffff)==0xffffff||(im.getRGB(t.x+1,t.y)&0xffffff)==0xfffffd;
					d = (up?1:0)+(down?1:0)+(left?1:0)+(right?1:0);
				//	System.out.print(d+" ");
					if(d==0)	// no path available
					{
						deleteleaf1(p);
						if(leaves1==null)
						{
							System.out.println("No path available!");
							return;
						}
					}
					else if(d==1)	// only one path available
					{
						if(down)
						{
							if((im.getRGB(t.x-1,t.y)&0xffffff)==0xfffffe||(im.getRGB(t.x+1,t.y)&0xffffff)==0xfffffe)	// Left or right traversed
							{
								c = new node(t.x,t.y+1,t);
								addleaf1(c);
								deleteleaf1(p);
							}
							else
								t.y++;
						}
						else if(right)
						{
							if((im.getRGB(t.x,t.y-1)&0xffffff)==0xfffffe||(im.getRGB(t.x,t.y+1)&0xffffff)==0xfffffe)	// Up or down traversed
							{
								c = new node(t.x+1,t.y,t);
								addleaf1(c);
								deleteleaf1(p);
							}
							else
								t.x++;
						}
						else if(left)
						{
							if((im.getRGB(t.x,t.y-1)&0xffffff)==0xfffffe||(im.getRGB(t.x,t.y+1)&0xffffff)==0xfffffe)	// Up or down traversed
							{
								c = new node(t.x-1,t.y,t);
								addleaf1(c);
								deleteleaf1(p);
							}
							else
								t.x--;
						}
						else	// up
						{
							if((im.getRGB(t.x-1,t.y)&0xffffff)==0xfffffe||(im.getRGB(t.x+1,t.y)&0xffffff)==0xfffffe)	// Left or right traversed
							{
								c = new node(t.x,t.y-1,t);
								addleaf1(c);
								deleteleaf1(p);
							}
							else
								t.y--;
						}
					}
					else	// d>1, 2 or 3 paths available, branching of tree
					{
						if(down)
						{
							c = new node(t.x,t.y+1,t);
							addleaf1(c);
						}
						if(right)
						{
							c = new node(t.x+1,t.y,t);
							addleaf1(c);
						}
						if(left)
						{
							c = new node(t.x-1,t.y,t);
							addleaf1(c);
						}
						if(up)
						{
							c = new node(t.x,t.y-1,t);
							addleaf1(c);
						}
						deleteleaf1(p);
					}
				}

			//	System.out.println("\nActive leaves of reverse tree");
				for(p=leaves2;p!=null;p=p.next)
				{
					t=p.data;
				//	System.out.print("("+t.x+","+t.y+") ");
					if((im.getRGB(t.x,t.y)&0xffffff)==0xfffffe)
					{
						for(q=leaves1;q!=null;q=q.next)
							if(t.x==q.data.x&&Math.abs(t.y-q.data.y)==1||t.y==q.data.y&&Math.abs(t.x-q.data.x)==1)
							{
								System.out.println("Path found!");
								target1=q.data;
								target2=t;
								return;
							}
					}
					im.setRGB(t.x,t.y,0xfffffd);
					up = (im.getRGB(t.x,t.y-1)&0xffffff)==0xffffff||(im.getRGB(t.x,t.y-1)&0xffffff)==0xfffffe;
					down = (im.getRGB(t.x,t.y+1)&0xffffff)==0xffffff||(im.getRGB(t.x,t.y+1)&0xffffff)==0xfffffe;
					left = (im.getRGB(t.x-1,t.y)&0xffffff)==0xffffff||(im.getRGB(t.x-1,t.y)&0xffffff)==0xfffffe;
					right = (im.getRGB(t.x+1,t.y)&0xffffff)==0xffffff||(im.getRGB(t.x+1,t.y)&0xffffff)==0xfffffe;
					d = (up?1:0)+(down?1:0)+(left?1:0)+(right?1:0);
				//	System.out.print(d+" ");
					if(d==0)	// no path available
					{
						deleteleaf2(p);
						if(leaves2==null)
						{
							System.out.println("No path available!");
							return;
						}
					}
					else if(d==1)	// only one path available
					{
						if(down)
						{
							if((im.getRGB(t.x-1,t.y)&0xffffff)==0xfffffd||(im.getRGB(t.x+1,t.y)&0xffffff)==0xfffffd)	// Left or right traversed
							{
								c = new node(t.x,t.y+1,t);
								addleaf2(c);
								deleteleaf2(p);
							}
							else
								t.y++;
						}
						else if(right)
						{
							if((im.getRGB(t.x,t.y-1)&0xffffff)==0xfffffd||(im.getRGB(t.x,t.y+1)&0xffffff)==0xfffffd)	// Up or down traversed
							{
								c = new node(t.x+1,t.y,t);
								addleaf2(c);
								deleteleaf2(p);
							}
							else
								t.x++;
						}
						else if(left)
						{
							if((im.getRGB(t.x,t.y-1)&0xffffff)==0xfffffd||(im.getRGB(t.x,t.y+1)&0xffffff)==0xfffffd)	// Up or down traversed
							{
								c = new node(t.x-1,t.y,t);
								addleaf2(c);
								deleteleaf2(p);
							}
							else
								t.x--;
						}
						else	// up
						{
							if((im.getRGB(t.x-1,t.y)&0xffffff)==0xfffffd||(im.getRGB(t.x+1,t.y)&0xffffff)==0xfffffd)	// Left or right traversed
							{
								c = new node(t.x,t.y-1,t);
								addleaf2(c);
								deleteleaf2(p);
							}
							else
								t.y--;
						}
					}
					else	// d>1, 2 or 3 paths available, branching of tree
					{
						if(down)
						{
							c = new node(t.x,t.y+1,t);
							addleaf2(c);
						}
						if(right)
						{
							c = new node(t.x+1,t.y,t);
							addleaf2(c);
						}
						if(left)
						{
							c = new node(t.x-1,t.y,t);
							addleaf2(c);
						}
						if(up)
						{
							c = new node(t.x,t.y-1,t);
							addleaf2(c);
						}
						deleteleaf2(p);
					}
				}
			//	Thread.sleep(250);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	void addleaf1(node c)
	{
		try
		{
			listnode t = new listnode(c,leaves1);
			leaves1.previous=t;
			leaves1=t;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	void deleteleaf1(listnode p)
	{
		try
		{
			if(p==leaves1)	// If the first node is to be deleted
			{
				leaves1=leaves1.next;
				if(leaves1!=null)
					leaves1.previous=null;
			}
			else
			{
				listnode o=p.previous;
				listnode q=p.next;
				o.next=q;
				if(q!=null)
					q.previous=o;
				p.previous=null;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	void addleaf2(node c)
	{
		try
		{
			listnode t = new listnode(c,leaves2);
			leaves2.previous=t;
			leaves2=t;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	void deleteleaf2(listnode p)
	{
		try
		{
			if(p==leaves2)	// If the first node is to be deleted
			{
				leaves2=leaves2.next;
				if(leaves2!=null)
					leaves2.previous=null;
			}
			else
			{
				listnode o=p.previous;
				listnode q=p.next;
				o.next=q;
				if(q!=null)
					q.previous=o;
				p.previous=null;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public void traceback()
	{
		try
		{
			int x=0,y=0;
			node t=null;
			String s1="",s2="";
			if(target1!=null)
			{
				t=target1.parent;
				x=target1.x;
				y=target1.y;
				s1="("+x+","+y+")"; 
				while(t!=null)
				{
					while(x!=t.x||y!=t.y)
					{
						im.setRGB(x,y,0x00ff00);
						if(x<t.x)	x++;
						else if(x>t.x)	x--;	// Don't take action if values are equal
						if(y<t.y)	y++;
						else if(y>t.y)	y--;
					}
					s1="("+x+","+y+") -> "+s1;
					t=t.parent;
				}
				while(x!=sx||y!=sy)
				{
					im.setRGB(x,y,0x00ff00);
					if(x<sx)	x++;
					else if(x>sx)	x--;	// Don't take action if values are equal
					if(y<sy)	y++;
					else if(y>sy)	y--;
				}
				if(!s1.startsWith("("+sx+","+sy+")"))
					s1="("+sx+","+sy+") -> "+s1;
			}
			if(target2!=null)
			{
				t=target2.parent;
				x=target2.x;
				y=target2.y;
				s2="("+x+","+y+")"; 
				while(t!=null)
				{
					while(x!=t.x||y!=t.y)
					{
						im.setRGB(x,y,0x00ff00);
						if(x<t.x)	x++;
						else if(x>t.x)	x--;	// Don't take action if values are equal
						if(y<t.y)	y++;
						else if(y>t.y)	y--;
					}
					s2=s2+" -> ("+x+","+y+")";
					t=t.parent;
				}
				while(x!=ex||y!=ey)
				{
					im.setRGB(x,y,0x00ff00);
					if(x<ex)	x++;
					else if(x>ex)	x--;	// Don't take action if values are equal
					if(y<ey)	y++;
					else if(y>ey)	y--;
				}
				if(!s2.endsWith("("+ex+","+ey+")"))
					s2=s2+" -> ("+ex+","+ey+")";
			}
			System.out.println("Path: "+s1+" -> "+s2);
			im.setRGB(sx,sy,0x007fff);
			im.setRGB(ex,ey,0xff0000);
			ImageIO.write(im,"bmp",new File("mazesolnmod.bmp"));
			System.out.println("Time taken: "+(System.currentTimeMillis()-stime)+" ms");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String arg[])
	{
		MazeSolver o=new MazeSolver();
		o.init();
		o.solve();
		o.traceback();
	}
}
