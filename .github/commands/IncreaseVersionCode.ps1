[CmdletBinding()]
param (
    $branchName,
	$versionNameFileName,
	$versionCodeFileName
)

$commitHashFolderName = "output"
$commitHashFileName = "commitHash.txt"

Write-Host "Checking out: $($branchName)"
git checkout $($branchName)
git pull

$commitMessage = ""

$file = $versionNameFileName

$updatedFileContent =
  switch -regex -file $file
  { # Loop over all lines in the file.
	'val appVersionCode by extra { .*' { # line with version name

	  # Extract the version name...
	  $versionCode = $Matches[0].Split("{")[1] -replace '"', '' -replace '\s',''
	  Write-Host "Version name: $versionCode"

	  # Pass them through.
	  $_
	}
	default { # All other lines.
	  # Pass them through.
	  $_
	}
  }

$file = $versionCodeFileName

$updatedFileContent =
  switch -regex -file $file
  { # Loop over all lines in the file.
	'val appVersionCode by extra { \d{1,}$' { # line with version code

	  # Extract the old version number...
	  $oldVersion = [int]$Matches[0].Split("{")[1]
	  Write-Host "Old version: $oldVersion"

	  $newVersion = $oldVersion + 1
	  Write-Host "New version: $newVersion"

	  # Replace the old version with the new version in the line
	  # and output the modified line.
	  $_.Replace($oldVersion.ToString(), $newVersion.ToString())
	}
	default { # All other lines.
	  # Pass them through.
	  $_
	}
  }

$versionName = $versionCode

Write-Host "##vso[task.setvariable variable=versionCode]$newVersion"
Write-Host "##vso[task.setvariable variable=versionName]$versionName"

$commitMessage = "Updated to version $versionName ($newVersion) [skip ci]"

# Save back to file. Use -Encoding as needed.
Write-Host "New gradle file: $updatedFileContent"
$updatedFileContent | Set-Content $file

git add .

git commit -m $commitMessage
git push

# Save commit hash to file
$commitHash = (git rev-parse HEAD).Substring(0,16)
Write-Host "Commit hash: $commitHash"

New-Item -Name $commitHashFolderName -ItemType "directory"
New-Item -Path $commitHashFolderName -Name $commitHashFileName

$commitHash | Set-Content "output\commitHash.txt"
