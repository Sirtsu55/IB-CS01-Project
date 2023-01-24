# IB-CS01-Project


## Target assessment level

* Target assessment level of this work is 3.


## What does the program do?

The program reads data of a scene with a sphere from a text file that the user supplied and processes it.
The program generates a ray-traced image of the described scene with a sphere. It stores the image in output.png, relative to where the program is executed.

When asked an output file, enter a suitable name for the resulting png the program creates

When asked an settings file, enter the relative path to the file that contains the settings

The user can generate as many raytraced scenes as they want, with different settings files, because the program loops and asks again for the fields until the user hits enter, which will cause the program to close

This fulfills the criteria
>At least some of the data is read from the user or from a file using a loop

Example input with provided files:

    Enter an output file name, or enter to exit
    >>outImage
    Enter a Settings name, or enter to exit
    >>Settings.txt

This will generate a outImage.png with the raytraced image 
and read the scene data from Settings.txt
User input is denoted by `>>` 


### Data format

There are data fields in the settings file with a name and decimals/integers seperated by a space eg.

    SphereColor 1.0 1.0 1.0
    SpherePosition 0.0 0.0 -2.0
    SphereRadius 1.0
    CameraPosition 0.0 0.0 0.0
    FocalLength -1.0
    LightPosition 5.0 100.0 -2.0
    LightIntensity 10000.0
    Dimentions 1280 720

The input order cannot be changed and there must be the same number of values per data field as demonstrated in the example above, for example 3 floating point values for SphereColor and 1 floating point for SphereRadius

floating point fields accept integers, but integer fields don't accept floating point values

Breakdown of input:
*    SphereColor: RGB values from 0 - 1 describing sphere's color
*    SpherePosition XYZ values describing where the sphere is located
*    SphereRadius Radius of the sphere
*    CameraPosition XYZ value describing where the image should be taken from
*    FocalLength Focal length of a camera
*    LightPosition Where the light should be located
*    LightIntensity What the intesity of the light is
*    Dimentions integer values describing the resolution of the output image


### Error Handling (level 2 and 3)
*   If the program cannot find the settings file supplied, it will report it and quit
*   If an error happens while parsing the input file, it will inform the user, on which stage it went wrong and quits the program 
*   If and unexpected error is caught while parsing, it will be caught and reported to the user
*   The program expects the inputs to be in the order that is supplied the example settings, else a parsing error will be invoked
*   Parsing errors are reported using the following format `Parsing Failed on {Parsing Stage}` where Parsing Stage is one of the input fields
*   If the program cannot create/write to an output image, the user will be notified
*   If a floating value is supplied instead of a integer, a parsing error will occur
*   If a bizzare output dimention is provided as input, then the system can run out of memory to store the image

### Resource Management (level 3)
*   Scanner is used to read from the file and always closed before exitting
*   BufferedImage is used to make an array to store the image
*   After BufferedImage is created, the program will not exit mid-execution, ensuring the deletion of the memory and object
*   All java objects will be destroyed with the Garbage collector
*   When a parsing error happens, the program will close the Scanner, before quitting the program
*   The while loop inside main() will make sure the Scanner is closed before the program has the ability to exit with a parsing error

###


