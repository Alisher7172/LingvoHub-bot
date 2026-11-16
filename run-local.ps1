# Run LingvoHub locally using application-local.yml (see application-local.example.yml)
Set-Location $PSScriptRoot
$localConfig = Join-Path $PSScriptRoot 'application-local.yml'

if (-not (Test-Path $localConfig)) {
    $existingResourceConfig = Join-Path $PSScriptRoot 'src/main/resources/application-local.yml'
    if (Test-Path $existingResourceConfig) {
        Copy-Item $existingResourceConfig $localConfig
    } else {
        Copy-Item (Join-Path $PSScriptRoot 'src/main/resources/application-local.example.yml') $localConfig
    }
    Write-Host 'Created application-local.yml. Add your bot token, username, and admin IDs, then run this script again.'
    exit 1
}

mvn spring-boot:run "-Dspring-boot.run.profiles=local"
exit $LASTEXITCODE
