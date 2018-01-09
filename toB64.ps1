$command = Read-Host 'Enter command string to encode: '
$bytes = [System.Text.Encoding]::Unicode.GetBytes($command)
$encodedCommand = [Convert]::ToBase64String($bytes)
Write-Host $encodedCommand
