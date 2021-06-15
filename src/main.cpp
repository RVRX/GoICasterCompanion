#include <iostream>
#include <unistd.h>
#include <string>

// Team Definitions
#define RED 0
#define TEAM_A 0
#define BLUE 1
#define TEAM_B 1


bool setTeam(const char* teamName, int teamNumber);

int main() {
    printf("Welcome to GoIStreamToolRedux CLI\n");

    // demo cases
    bool success = setTeam("The Skyborne", RED);
    return 0;
}

/**
 * Sets the Team for a specific team color/number
 * E.g. used when setting "The Skyborne" as blue team (where blue team is represented as a number).
 *
 * @param teamName Name of the team
 * @param teamNumber What team position this team will fill
 * @return
 */
bool setTeam(const char* teamName, int teamNumber) {
    printf("Setting Team %d as %s", teamNumber, teamName);
    return false;
}
