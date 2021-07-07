#!/usr/bin/env lua
--[[
Update application to new version
changes all the variables and meta-data
to point to a new version of the project

To see specific changes this script makes: https://github.com/RVRX/GoICasterCompanion/issues/74
NOTE: Does not currently update the website
--]]


-- Helper fcn for editing files, hardcoded modifiers
function findAndEditLine(filePath, modifierType)
	local hFile = io.open(filePath, "r") --Reading.
	local lines = {}
	local restOfFile
	local lineCt = 1
	for line in hFile:lines() do

		--comparison
		if (modifierType == 0) then --App.java
			lineCheck = string.find(line,'public static final String version = "')
			lineReplacement = '    public static final String version = "' .. newVersion .. '";'
		elseif (modifierType == 1) then --Inno Script
			lineCheck = string.find(line,'#define MyAppVersion "')
			lineReplacement = '#define MyAppVersion "' .. newVersion .. '"'
		elseif (modifierType == 2) then
			lineCheck = string.find(line, "project.version = '")
			lineReplacement = "project.version = '" .. newVersion .. "'"
		end

		--modifier

	  if(lineCheck) then --Is this the line to modify?
	    lines[#lines + 1] = lineReplacement --Change old line into new line.
	    restOfFile = hFile:read("*a")
	    break
	  else
	    lineCt = lineCt + 1
	    lines[#lines + 1] = line
	  end
	end
	hFile:close()

	hFile = io.open(filePath, "w") --write the file.
	for i, line in ipairs(lines) do
	  hFile:write(line, "\n")
	end
	if (not (restOfFile == nil)) then
		hFile:write(restOfFile)
	end
	hFile:close()
end


-- get the new version number from user
if (arg[1] == nil) then
	print('New Version Number:')
	newVersion = io.read()
else 
	newVersion = arg[1]
end
io.write('Updating project files to version: "', newVersion, '"...\n')


-- ** UPDATE "APP.JAVA" **
io.stdout:write('	Updating "App.java"\n')
findAndEditLine("src/main/java/goistreamtoolredux/App.java", 0)

-- ** UPDATE INNO SCRIPT **
io.stdout:write('	Updating Inno Installation Script\n')
findAndEditLine("GoICC Installation Media Creation Script.iss", 1)

-- ** UPDATE "BUILD.GRADLE" **
io.stdout:write('	Updating "build.gradle"\n')
findAndEditLine("build.gradle", 2)

-- ** UPDATE "CURRENTVERSION" **
io.stdout:write('	Updating "CURRENTVERSION" file\n')
currentVersionTxtFile = io.open("CURRENTVERSION", "w+") -- Write enabled mode that overwrites the existing file or creates a new file.
io.output(currentVersionTxtFile)
io.write(newVersion) -- write new version to file
io.close(currentVersionTxtFile)

-- ** UPDATE WEBSITE **
-- @TODO


print('Process Finished.')

