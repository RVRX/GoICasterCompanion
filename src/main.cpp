#include <iostream>
#include <unistd.h>
#include <string>
#include <fstream>
#include <dirent.h>
#include <sys/stat.h>
#include <regex.h>

#define LOGODIR "./input/team_logos/"



char * isFile(char *dir, int depth, const char *fileName);
bool setTeam(const char* teamName, const char* teamNumber);
int copyFile(const char *originalLocation, const char *copyLocation);
using std::string;
string getFileExt(const string& s);


int main() {
    printf("Welcome to GoIStreamToolRedux CLI\n");

    // demo cases
    bool success = setTeam("The Skyborne", "A");

    return 0;
}

/**
 * Sets the Team for a specific team color/number
 * E.g. used when setting "The Skyborne" as blue team (where blue team is represented as a number).
 *
 * @param teamName Name of the team, MUST BE A VALID TEAM NAME
 * @param teamNumber What team position this team will fill
 * @return
 */
bool setTeam(const char* teamName, const char* teamNumber) {
    printf("setTeam(): Setting Team %s as %s...\n", teamNumber, teamName);

    //--- update TeamX.png ---
    // Looks for `"input/team_logos/team" + teamNumber`, with any extension.
    // If found, moves it to `"output/team" + teamNumber + "." + foundLogoExt`
    printf("           Updating Image...\n");

    char logoDir[20] = LOGODIR;
    char fileName[25] = "team";
    strcat(fileName, teamNumber); //gives us "team" + teamNumber

    //todo lookup correct team name instead of using placeholder
    char foundLogoName[strlen(isFile(logoDir, 0, teamName)) + 1]; //todo more efficient way than running isFile() twice.
    strcpy(foundLogoName, isFile(logoDir, 0, teamName)); //returns name of file with extension or null
    if (strlen(foundLogoName) < 1) {
        printf("Logo file could not be found");
        return false;
    }
    printf("           ... Found image \"%s\"\n", foundLogoName);

    //get foundLogoNames' file extension.
    char foundLogoExt[strlen(getFileExt(foundLogoName).c_str())];
    strcpy(foundLogoExt, getFileExt(foundLogoName).c_str());
    printf("           ... Image has extension: %s\n", foundLogoExt);


    //copy found file to output folder

    // Full path to file output destination of selected team's image
    char teamImageOutputPath[strlen(fileName) + strlen(foundLogoExt) + 12]; //create char[]
    snprintf(teamImageOutputPath, sizeof(teamImageOutputPath), "./output/%s.%s", fileName, foundLogoExt); // define ^

    //Full path to file input of selected team's image
    char teamImageInputPath[100];
    snprintf(teamImageInputPath, 100, "%s%s.%s", LOGODIR, teamName, foundLogoExt); //

    printf("           ... Copying image from \"%s\" to \"%s\"\n", teamImageInputPath, teamImageOutputPath);
    copyFile(teamImageInputPath, teamImageOutputPath); //copy file to output //todo if output folder doesn't exist, this will error


    //--- update TeamX.txt ---
    //output to "team" + teamNumber + ".txt"


    //output file stream, truncate (overwrite current contents)
    std::ofstream ofs(fileName, std::ofstream::trunc);
    ofs << teamName;
    ofs.close();

    //todo update TeamShortX.txt

    return false;
}

/**
 * Used to check if a filename exists in a directory, regardless of extension.
 *
 * Adapted from https://stackoverflow.com/questions/8149569/scan-a-directory-to-find-files-in-c
 * @param dir
 * @param depth
 * @return NULLPTR if not found, full filename and extension if found.
 */
char* isFile(char *dir, int depth, const char *fileName) {
    DIR *dp;
    struct dirent *entry;
    struct stat statbuf;
    if((dp = opendir(dir)) == NULL) {
        fprintf(stderr,"cannot open directory: %s\n", dir);
        return nullptr; //todo NULL vs nullptr
    }
    chdir(dir);
    while((entry = readdir(dp)) != NULL) {
        lstat(entry->d_name,&statbuf);
        if(S_ISDIR(statbuf.st_mode)) {
            /* Found a directory, but ignore . and .. */
            if (strcmp(".",entry->d_name) == 0 || strcmp("..",entry->d_name) == 0) { continue; }
            if (strncmp(fileName,entry->d_name,strlen(fileName)) == 0) { //if d_name == the fileName, only comparing the length of fileName so as to avoid extension
//                printf("found"); ///(.*)\.[^.]+$/
//                printf("HERE %s", entry->d_name);

                chdir("../..");
                return entry->d_name;
            }
            //printf("%*s%s/\n",depth,"",entry->d_name);
            /* Recurse at a new indent level */
            isFile(entry->d_name, depth + 4, fileName);
        }
        else {
            if (strncmp(fileName,entry->d_name,strlen(fileName)) == 0) { //if d_name == the fileName, only comparing the length of fileName so as to avoid extension
//                printf("found"); ///(.*)\.[^.]+$/
//                printf("HERE %s", entry->d_name);

                chdir("../..");
                return entry->d_name;
            }
            //printf("%*s%s\n",depth,"",entry->d_name);
        }
    }
    chdir("../.."); //change back to right dir
    closedir(dp);
    return nullptr;
}

/**
 * Copies file from one place to another.
 * @param originalLocation
 * @param copyLocation
 * @return
 */
int copyFile(const char *originalLocation, const char *copyLocation) {
        FILE *original,*copy;
        int c;
        original=fopen(originalLocation,"r");
        copy=fopen(copyLocation,"w");
        if( !original || !copy)
        {
            puts("File error!");
            exit(1);
        }
        while( (c=fgetc(original)) != EOF)
            fputc(c,copy);
        puts("File duplicated");
        return(0);
}

/**
 *
 * @param s
 * @return
 */
string getFileExt(const string& s) {

    size_t i = s.rfind('.', s.length());
    if (i != string::npos) {
        return(s.substr(i+1, s.length() - i));
    }

    return("");
}
