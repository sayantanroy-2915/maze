import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;

class MazeGenerator
{
	static boolean maze[][];
	static int n;

	static void partition(int x1,int y1,int x2, int y2)
	{
		int p=0,q=0,i=0,k=0;
	//	System.out.println("Partition "+x1+","+y1+" "+x2+","+y2);
		if(x2-x1<2||y2-y1<2)
			return;
		Random r = new Random();
		if(r.nextInt(2)==0)
		{
			do{
				p = r.nextInt(x2-x1);
			}while(p%2==0);
			for(i=y1;i<=y2;i++)
				maze[x1+p][i]=true;
			System.out.println("Partitioned column "+(x1+p)+" between rows "+y1+","+y2);
			k = (int)Math.sqrt(Math.sqrt(y2-y1));
			for(i=0;i<k;i++)
			{
				do{
					q = r.nextInt(y2-y1);
				}while(q%2==1);
				maze[x1+p][y1+q]=false;
				System.out.println("Path created at ("+(x1+p)+","+(y1+q)+")");
			}
			partition(x1,y1,x1+p-1,y2);
			partition(x1+p+1,y1,x2,y2);
		}
		else
		{
			do{
				p = r.nextInt(y2-y1);
			}while(p%2==0);
			for(i=x1;i<=x2;i++)
				maze[i][y1+p]=true;
			System.out.println("Partitioned row "+(y1+p)+" between columns "+x1+","+x2);
			k = (int)Math.sqrt(Math.sqrt(x2-x1));
			for(i=0;i<k;i++)
			{	
				do{
					q = r.nextInt(x2-x1);
				}while(q%2==1);
				maze[x1+q][y1+p]=false;
				System.out.println("Path created at ("+(x1+q)+","+(y1+p)+")");
			}
			partition(x1,y1,x2,y1+p-1);
			partition(x1,y1+p+1,x2,y2);
		}
	}

	public static void main(String arg[])
	{
		Scanner sc=new Scanner(System.in);
		System.out.print("Enter size of maze: ");
		int sx=0,sy=0,ex=0,ey=0;
		n = sc.nextInt();
		n = n%2==0?(n+1):n;
		maze = new boolean[n+2][n+2];
		for(int i=0;i<n+2;i++)
		{
			maze[i][0]=true;
			maze[0][i]=true;
			maze[i][n+1]=true;
			maze[n+1][i]=true;
		}
		partition(1,1,n,n);
		try{
			BufferedImage im = new BufferedImage(n+2,n+2,BufferedImage.TYPE_INT_RGB);
			for(int i=0;i<n+2;i++)
				for(int j=0;j<n+2;j++)
					if(maze[i][j])
						im.setRGB(j,i,0);
					else
						im.setRGB(j,i,0xffffff);
			do
			{
				System.out.print("Enter starting point: ");
				if(sc.hasNextInt())
					sx=sc.nextInt();
				if(sc.hasNextInt())
					sy=sc.nextInt();
			}while((sx>n+1)||(sy>n+1)||(im.getRGB(sx,sy)&0xffffff)==0);
			im.setRGB(sx,sy,0x007fff);
			FileWriter fw=new FileWriter("maze.txt");
			fw.write(""+sx+" "+sy);
			do
			{
				System.out.print("Enter destination point: ");
				if(sc.hasNextInt())
					ex=sc.nextInt();
				if(sc.hasNextInt())
					ey=sc.nextInt();
			}while((ex>n+1)||(ey>n+1)||(im.getRGB(ex,ey)&0xffffff)==0||(ex==sx&&ey==sy));
			im.setRGB(ex,ey,0xff0000);
			fw.write(" "+ex+" "+ey);
			fw.close();
			ImageIO.write(im,"bmp",new File("maze.bmp"));
		}catch(Exception e){};
	}
}
