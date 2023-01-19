import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

class Vector3
{
    public double x;
    public double y;
    public double z;
    public Vector3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double Length()
    {
        //Length of a vector
        return Math.sqrt(x*x + y*y + z*z);
    }
    public Vector3 Normalize()
    {
        double length = Math.sqrt(x*x + y*y + z*z);
        return new Vector3(x /= length, y /= length, z /= length);
    }
    public Double Dot(Vector3 other)
    {
        return x * other.x + y * other.y + z * other.z;
    }
    public Vector3 Add(Vector3 other)
    {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }
    public Vector3 Subtract(Vector3 other)
    {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }
    public Vector3 Multiply(Vector3 other)
    {
        return new Vector3(x * other.x, y * other.y, z * other.z);
    }
    public Vector3 Multiply(double value)
    {
        return new Vector3(x * value, y * value, z * value);
    }
}
class Vector2
{
    public double x;
    public double y;
    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public Vector2 Normalize()
    {
        double length = Math.sqrt(x*x + y*y);
        return new Vector2(x /= length, y /= length);
    }


}

class Ray
{
    Ray(Vector3 origin, Vector3 direction)
    {
        this.origin = origin;
        this.direction = direction;
    }
    public Vector3 origin;
    public Vector3 direction;
}

class ParsedSettings
{
    public int DimentionX;
    public int DimentionY;
    public Vector3 SpherePosition;
    public Vector3 SphereColor;
    public double SphereRadius;
    public Vector3 CameraPosition;
    public Vector3 LightPosition;
    public double LightIntensity;
    public double FocalLength;

}
class Intersection
{
    public double Distance;
    public Vector3 Normal;
    public Vector3 Point;
}

class RayTracer
{
    private ParsedSettings mSettings;
    private BufferedImage mImage;
    RayTracer(String file)
    {
        ParseFile(file);
        
        mImage = new BufferedImage(mSettings.DimentionX, mSettings.DimentionY, BufferedImage.TYPE_INT_RGB);

    }
    private double GetDouble(Scanner scanner, String state)
    {
        scanner.useLocale(Locale.US); //use the default locale (en_US)   
        try
        {
            double x = scanner.nextDouble();
            return x;
        }
        catch(Exception e)
        {
            System.out.println("Parsing Failed on " + state);
            System.exit(-1);
        }
        return 0.0;
    }
    private int GetInt(Scanner scanner, String state)
    {
        scanner.useLocale(Locale.US); //use the default locale (en_US)   
        try
        {
            int x = scanner.nextInt();
            return x;
        }
        catch(Exception e)
        {
            System.out.println("Parsing Failed on " + state);
            System.exit(-1);
        }
        return -1;
    }
    private Vector3 GetVector(Scanner scanner, String state)
    {
        scanner.useLocale(Locale.US); //use the default locale (en_US)   
        try
        {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double z = scanner.nextDouble();
            return new Vector3(x, y, z);
        }
        catch(Exception e)
        {
            System.out.println("Parsing Failed on " + state);
            System.exit(-1);
        }
        return new Vector3(0,0,0);
    }

    //make a function to open a file .txt
    private ParsedSettings ParseFile(String path)
    {
     
     File file = new File(path);
     mSettings = new ParsedSettings();
     try
     {
        Scanner inp = new Scanner(file);
        //scan for sphere info
        if(inp.hasNextLine())
        {
            if(inp.next().equals("SphereColor"))
                mSettings.SphereColor = GetVector(inp, "Sphere Color");
            else 
            {
                System.out.println("Parsing Failed on Sphere Color");
                System.exit(-1);
            }
            if(inp.next().equals("SpherePosition"))
                mSettings.SpherePosition = GetVector(inp, "Sphere Position");
            else 
            {
                System.out.println("Parsing Failed on Sphere Position");
                System.exit(-1);
            }
            if(inp.next().equals("SphereRadius"))
                mSettings.SphereRadius = GetDouble(inp, "Sphere Radius");
            else
            {
                System.out.println("Parsing Failed on Sphere Radius");
                System.exit(-1);
            }
            if(inp.next().equals("CameraPosition"))
                mSettings.CameraPosition = GetVector(inp, "Camera Position");
            else
            {
                System.out.println("Parsing Failed on Camera Position");
                System.exit(-1);
            }
            if(inp.next().equals("FocalLength"))
                mSettings.FocalLength = GetDouble(inp, "Focal Length");
            else
            {
                System.out.println("Parsing Failed on Focal Length");
                System.exit(-1);
            }
            if(inp.next().equals("LightPosition"))
                mSettings.LightPosition = GetVector(inp, "Light Position");
            else
            {
                System.out.println("Parsing Failed on Light Position");
                System.exit(-1);
            }
            if(inp.next().equals("LightIntensity"))
                mSettings.LightIntensity = GetDouble(inp, "Light Intensity");
            else
            {
                System.out.println("Parsing Failed on Light Intensity");
                System.exit(-1);
            }
            if(inp.next().equals("Dimentions"))
            {
                mSettings.DimentionX = GetInt(inp, "Dimention X");
                mSettings.DimentionY = GetInt(inp, "Dimention Y");
                if(mSettings.DimentionX <= 0)
                {
                    System.out.println("Invalid Dimention X value");
                    System.exit(-1);
                }
                if(mSettings.DimentionY <= 0)
                {
                    System.out.println("Invalid Dimention Y value");
                    System.exit(-1);
                }
            }
            else
            {
                System.out.println("Parsing Failed on Dimention");
                System.exit(-1);
            }
            
        }
        else
        {
            System.out.println("Parsing Failed is empty");
            System.exit(-1);
        }

        inp.close();
     }
     catch(FileNotFoundException e)
     {
         System.out.println("File does not exist");
         System.exit(-1);
     }
     return mSettings;
    }
 
    public void RayTrace()
    {
        double aspectRatio = (double)mSettings.DimentionX / (double)mSettings.DimentionY;
        double vpHeight = 2.0;
        double vpWidth = aspectRatio * vpHeight;
        
        for(int i = 0; i < mSettings.DimentionX; i++)
        {
            for(int j = 0; j < mSettings.DimentionY; j++)
            {

                //scrachapixel camera
                Vector2 uv = new Vector2((i + 0.5)/(double)mSettings.DimentionX, (j + 0.5)/(double)mSettings.DimentionY);

                //calculate ray direction
                Ray ray = new Ray(mSettings.SpherePosition, new Vector3(0, 0, 0));
                ray.origin = mSettings.CameraPosition;  
                
                Vector3 horizontal = new Vector3(vpWidth, 0, 0);
                Vector3 vertical = new Vector3(0, vpHeight, 0);
                Vector3 focalLength = new Vector3(0, 0, mSettings.FocalLength);
                Vector3 lowerLeftCorner = ray.origin.Subtract(horizontal.Multiply(0.5)).Subtract(vertical.Multiply(0.5)).Subtract(focalLength);
                ray.direction = lowerLeftCorner.Add(horizontal.Multiply(uv.x)).Add(vertical.Multiply(uv.y)).Subtract(ray.origin);


                Intersection ix = IntersectsSphere(ray, mSettings.SpherePosition, mSettings.SphereRadius);
                int color = 0;

                
                if( ix.Distance != -1.0)
                {
                    
                    double light = CalculatePointLight(ix, mSettings.LightPosition, mSettings.LightIntensity);
                    
                    //make sure not to go over 1.0, which is brightest and can overflow bitshifting
                    if(light > 1.0)
                    light = 1.0;
                    
                    Vector3 col = mSettings.SphereColor.Multiply(light);
                    //bitshift to get color
                    color = (int)(col.x * 255.0) << 16 | (int)(col.y * 255.0) << 8 | (int)(col.z * 255.0);   

                    mImage.setRGB(i, j, color);
                }
                else
                {

                    mImage.setRGB(i, j, 0x000000);
                }

            }
        }
    }
    //Point light
    public double CalculatePointLight(Intersection ix, Vector3 lightPos, double lightIntensity)
    {


        //inverse root formula
        Vector3 lightDir = lightPos.Subtract(ix.Point).Normalize();


        double intensityFactor = Math.max(lightDir.Dot(ix.Normal.Normalize()), 0.0);        

        double lightDistance = lightPos.Subtract(ix.Point).Length();

        double lightVal = (lightIntensity * intensityFactor) / (lightDistance * lightDistance);
        return lightVal;
    }


    public static Intersection IntersectsSphere(Ray ray, Vector3 position, double radius)
    {
        //check if intersects
        Intersection outIntersection = new Intersection();
        Vector3 oc = ray.origin.Subtract(position);
        
        double a = ray.direction.Dot(ray.direction);
        
        double b = 2.0 * oc.Dot(ray.direction);
        
        double c = oc.Dot(oc) - radius * radius;

        double discriminant = b * b - 4 * a * c;
        if(discriminant < 0)
        {
            outIntersection.Distance = -1.0;
        }
        else
        {
            //return distance to sphere
            outIntersection.Distance = (-b - Math.sqrt(discriminant)) / (2.0 * a);
            double otherDist = (-b + Math.sqrt(discriminant)) / (2.0 * a);
            outIntersection.Distance = Math.max(outIntersection.Distance, otherDist);
            outIntersection.Point = ray.origin.Add(ray.direction.Multiply(outIntersection.Distance));

            
            //calculate normal
            ray.direction.Normalize();
            Vector3 N = outIntersection.Point.Subtract(new Vector3(0,0, -2)).Normalize();
            outIntersection.Normal =  new Vector3(N.x, N.y, N.z).Multiply(0.5).Add(new Vector3(0.5, 0.5, 0.5));

        }


        return outIntersection;
    }
    public void WriteOut(String path)
    {
        try
        {
            ImageIO.write(mImage, "png", new File(path));
        }
        catch(Exception e)
        {
            System.out.println("Error writing image");
            System.exit(-1);
        }
    }
}

class JavaProject
{
   

    public static void main(String[] args) 
    {

        RayTracer rt = new RayTracer("Settings.txt");
        

        rt.RayTrace();
        rt.WriteOut("output.png");
    }



}