import java.io.File;
import java.util.Locale;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

//utility class to make storing 3d vectors easier 
//and to make the math easier
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
    //normalize the vector into a unit vector
    public Vector3 Normalize()
    {
        double length = Math.sqrt(x*x + y*y + z*z);
        return new Vector3(x /= length, y /= length, z /= length);
    }
    //get dot product of two vectors
    public Double Dot(Vector3 other)
    {
        return x * other.x + y * other.y + z * other.z;
    }
    //add two vectors
    public Vector3 Add(Vector3 other)
    {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }
    //subtract two vectors
    public Vector3 Subtract(Vector3 other)
    {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }
    //multiply two vectors
    public Vector3 Multiply(Vector3 other)
    {
        return new Vector3(x * other.x, y * other.y, z * other.z);
    }
    //multiply a vector by a scalar
    public Vector3 Multiply(double value)
    {
        return new Vector3(x * value, y * value, z * value);
    }
}
//utility class to make storing 2d vectors easier
class Vector2
{
    public double x;
    public double y;
    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    //same as the 3d vector, but in 2d
    public Vector2 Normalize()
    {
        double length = Math.sqrt(x*x + y*y);
        return new Vector2(x /= length, y /= length);
    }


}
//Describe a ray
class Ray
{
    Ray(Vector3 origin, Vector3 direction)
    {
        this.origin = origin;
        this.direction = direction;
    }
    //ray origin, so where the ray starts
    public Vector3 origin;
    //ray direction, so where the ray is going
    public Vector3 direction;
}

//all the settings that are parsed from the file
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
//Describe an intersection
class Intersection
{
    //distance from the ray origin to the intersection
    public double Distance;
    //normal of the intersection
    public Vector3 Normal;
    //point of the intersection in 3d world space
    public Vector3 Point;
}
//Class that does the ray tracing
class RayTracer
{
    private ParsedSettings mSettings;
    private BufferedImage mImage;
    RayTracer(String file)
    {
        //parse the file
        ParseFile(file);
        //Create an output image with RGB values
        mImage = new BufferedImage(mSettings.DimentionX, mSettings.DimentionY, BufferedImage.TYPE_INT_RGB);

    }
    //helper function to get a double from the scanner
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
            scanner.close(); System.exit(-1); 
        }
        return 0.0;
    }
    //helper function to get an int from the scanner
    private int GetInt(Scanner scanner, String state)
    {
        scanner.useLocale(Locale.US); //use the default locale (en_US)   
        try
        {
            //get the next int from the scanner
            int x = scanner.nextInt();
            return x;
        }
        catch(Exception e)
        {
            System.out.println("Parsing Failed on " + state);
            scanner.close(); System.exit(-1); 
        }
        return -1;
    }
    //helper function to get a vector from the scanner
    private Vector3 GetVector(Scanner scanner, String state)
    {
        //use this to get a double from the scanner
        scanner.useLocale(Locale.US); //use the default locale (en_US)   
        try
        {
            //get the floating point numbers from the scanner
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            double z = scanner.nextDouble();
            return new Vector3(x, y, z);
        }
        catch(Exception e)
        {
            System.out.println("Parsing Failed on " + state);
            scanner.close(); System.exit(-1); 
        }
        return new Vector3(0,0,0);
    }

    //make a function to open a file .txt
    private ParsedSettings ParseFile(String path)
    {
     
     File file = new File(path);
     mSettings = new ParsedSettings();
     //check if the file exists
     if(!file.exists())
     {
        System.out.println("File does not exist");
        System.exit(-1);
     }

     try
     {
        //should not throw an exception, because we checked if the file exists
        Scanner inp = new Scanner(file);
        //scan for sphere info
        if(inp.hasNextLine())
        {
            //use equals to compare strings and make sure they are correct settings
            //first check if file has next line, if not then the other expressions will not be evaluated
            //therefore not throwing an exception, when otherwise it would throw an exception if 
            //the there is no next string  
            if(inp.hasNext() && inp.next().equals("SphereColor"))
                mSettings.SphereColor = GetVector(inp, "Sphere Color");
            else 
            {
                System.out.println("Parsing Failed on Sphere Color");
                inp.close(); System.exit(-1); 
            }
            if(inp.hasNext() && inp.next().equals("SpherePosition"))
                mSettings.SpherePosition = GetVector(inp, "Sphere Position");
            else 
            {
                System.out.println("Parsing Failed on Sphere Position");
                inp.close(); System.exit(-1); 
            }
            if(inp.hasNext() && inp.next().equals("SphereRadius"))
                mSettings.SphereRadius = GetDouble(inp, "Sphere Radius");
            else
            {
                System.out.println("Parsing Failed on Sphere Radius");
                inp.close(); System.exit(-1); 
            }
            if(inp.hasNext() && inp.next().equals("CameraPosition"))
                mSettings.CameraPosition = GetVector(inp, "Camera Position");
            else
            {
                System.out.println("Parsing Failed on Camera Position");
                inp.close(); System.exit(-1); 
            }
            if(inp.hasNext() && inp.next().equals("FocalLength"))
                mSettings.FocalLength = GetDouble(inp, "Focal Length");
            else
            {
                System.out.println("Parsing Failed on Focal Length");
                inp.close(); System.exit(-1); 
            }
            if(inp.hasNext() && inp.next().equals("LightPosition"))
                mSettings.LightPosition = GetVector(inp, "Light Position");
            else
            {
                System.out.println("Parsing Failed on Light Position");
                inp.close(); System.exit(-1); 
            }
            if(inp.hasNext() && inp.next().equals("LightIntensity"))
                mSettings.LightIntensity = GetDouble(inp, "Light Intensity");
            else
            {
                System.out.println("Parsing Failed on Light Intensity");
                inp.close(); System.exit(-1); 
            }
            if(inp.hasNext() && inp.next().equals("Dimentions"))
            {
                mSettings.DimentionX = GetInt(inp, "Dimention X");
                mSettings.DimentionY = GetInt(inp, "Dimention Y");
                if(mSettings.DimentionX <= 0)
                {
                    System.out.println("Invalid Dimention X value");
                    inp.close(); System.exit(-1); 
                }
                if(mSettings.DimentionY <= 0)
                {
                    System.out.println("Invalid Dimention Y value");
                    inp.close(); System.exit(-1); 
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
            //if the file is empty
            System.out.println("Parsing Failed " + file + "  is empty");
            System.exit(-1);
        }

        inp.close();
     }
     //catches if some unexpected error happens
     catch(Exception e)
     {
        System.out.println("Parsing failed with unexpected error message: " + e.getMessage());
        System.exit(-1);
     }
     return mSettings;
    }
 
    public void RayTrace()
    {
        //calculate aspect ratio, which is useful for the camera
        double aspectRatio = (double)mSettings.DimentionX / (double)mSettings.DimentionY;

        double vpHeight = 2.0;
        double vpWidth = aspectRatio * vpHeight;
        
        //loop through all the pixels in the image
        for(int i = 0; i < mSettings.DimentionX; i++)
        {
            for(int j = 0; j < mSettings.DimentionY; j++)
            {

                //scrachapixel camera
                Vector2 uv = new Vector2((i + 0.5)/(double)mSettings.DimentionX, (j + 0.5)/(double)mSettings.DimentionY);

                //calculate ray direction, which is the direction of the ray from the camera to the pixel
                Ray ray = new Ray(mSettings.SpherePosition, new Vector3(0, 0, 0));
                ray.origin = mSettings.CameraPosition;  
                
                //camera math is taken from ray tracing in one weekend
                //refer to the e-book for better understanding of the math

                Vector3 horizontal = new Vector3(vpWidth, 0, 0);
                Vector3 vertical = new Vector3(0, vpHeight, 0);
                Vector3 focalLength = new Vector3(0, 0, mSettings.FocalLength);
                Vector3 lowerLeftCorner = ray.origin.Subtract(horizontal.Multiply(0.5)).Subtract(vertical.Multiply(0.5)).Subtract(focalLength);
                ray.direction = lowerLeftCorner.Add(horizontal.Multiply(uv.x)).Add(vertical.Multiply(uv.y)).Subtract(ray.origin);

                //check for intersection
                Intersection ix = IntersectsSphere(ray, mSettings.SpherePosition, mSettings.SphereRadius);
                int color = 0;

                
                if( ix.Distance != -1.0)
                {
                    //calculate how much affect the light has on the pixel   
                    double light = CalculatePointLight(ix, mSettings.LightPosition, mSettings.LightIntensity);
                    
                    //make sure not to go over 1.0, which is brightest and can overflow bitshifting
                    //overflow will occupy more than 8 bits and cause the color to be incorrect
                    if(light > 1.0)
                    light = 1.0;
                    //multiply color by light, to get the color of the pixel
                    Vector3 col = mSettings.SphereColor.Multiply(light);
                    //bitshift to get color
                    //notice, the first 4 bits are not used, so the color is 24 bits
                    color = (int)(col.x * 255.0) << 16 | (int)(col.y * 255.0) << 8 | (int)(col.z * 255.0);   

                    mImage.setRGB(i, j, color);
                }
                else
                {
                    //if no intersection, set pixel to black
                    mImage.setRGB(i, j, 0x000000);
                }

            }
        }
    }
    //Point light calculation, uses inverse square law to calculate light, Doesn't use shadow rays
    public double CalculatePointLight(Intersection ix, Vector3 lightPos, double lightIntensity)
    {
        //Shadow rays are not used, so the light can be incorrect in certain areas
        //mainly behind the sphere, where the light should be blocked by the sphere
        //calculate where the light is in relation to the hit point
        Vector3 lightDir = lightPos.Subtract(ix.Point).Normalize();
        //calculate the intensity of the light, given the surface normal of the sphere
        double intensityFactor = Math.max(lightDir.Dot(ix.Normal.Normalize()), 0.0);        

        //calculate the distance from the light to the hit point
        double lightDistance = lightPos.Subtract(ix.Point).Length();

        //calculate the light value, using inverse square law
        double lightVal = (lightIntensity * intensityFactor) / (lightDistance * lightDistance);
        return lightVal;
    }


    public static Intersection IntersectsSphere(Ray ray, Vector3 position, double radius)
    {
        Intersection outIntersection = new Intersection();
        //check if intersects sphere

        //basically a quadratic equation for a 3D sphere at a given position in space
        // a ray is a line, so it can be represented as a point and a direction


        Vector3 oc = ray.origin.Subtract(position);
        
        double a = ray.direction.Dot(ray.direction);
        
        double b = 2.0 * oc.Dot(ray.direction);
        
        double c = oc.Dot(oc) - radius * radius;

        double discriminant = b * b - 4 * a * c;
        //if discriminant is less than 0, then there is no intersection
        if(discriminant < 0)
        {
            outIntersection.Distance = -1.0;
        }
        else
        {
            //if discriminant is greater than 0, then there is an intersection
            //calculate the distance to the intersection, by finishing the quadratic equation

            outIntersection.Distance = (-b - Math.sqrt(discriminant)) / (2.0 * a);
            double otherDist = (-b + Math.sqrt(discriminant)) / (2.0 * a);
            //chose the furthest intersection, so that the ray doesn't hit the sphere twice
            outIntersection.Distance = Math.max(outIntersection.Distance, otherDist);
            outIntersection.Point = ray.origin.Add(ray.direction.Multiply(outIntersection.Distance));

            
            //calculate normal vector, which is the direction of the surface normal at the hit point
            ray.direction.Normalize();
            Vector3 N = outIntersection.Point.Subtract(position).Normalize();
            outIntersection.Normal =  new Vector3(N.x, N.y, N.z).Multiply(0.5).Add(new Vector3(0.5, 0.5, 0.5));

        }


        return outIntersection;
    }
    //write the image to a file
    public void WriteOut(String path)
    {
        try
        {
            ImageIO.write(mImage, "png", new File(path));
            
        }
        catch(Exception e)
        {
            System.out.println("Error writing image: " + e.getMessage());
            //dont exit, just print error, because buffered image is still in memory   
        }
        

    }
}

class JavaProject
{
   

    public static void main(String[] args) 
    {

        //Create an raytracer with the settings file
        
        Scanner scanner = new Scanner(System.in);
        
        
        while(true)
        {
            //query for output file
            System.out.println("Enter an output file name, or enter to exit");
            String outFile = scanner.nextLine();
            
            if(outFile.equals(""))
                break;
            
            //query for settings file
            System.out.println("Enter a Settings name, or enter to exit");
            String settings = scanner.nextLine();
            if(settings.equals(""))
                break;

            RayTracer rt = new RayTracer(settings);
            //render the scene
            rt.RayTrace();
            //write the image to a file
            rt.WriteOut(outFile + ".png");
            //wait for enter to be pressed
        }

        scanner.close();
    }



}