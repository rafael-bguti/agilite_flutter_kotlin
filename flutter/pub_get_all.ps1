
function Execute-InSubDirectories {
    $subDirs = Get-ChildItem -Directory
    foreach ($dir in $subDirs) {
        # Trocar para o subdiretório
        Set-Location $dir.FullName

        # Executar o comando desejado
        Write-Host "Executando no diretório: $dir.FullName"
        # Seu comando aqui, exemplo: git status
        flutter clean
        flutter pub get

        # Voltar para o diretório inicial
        Set-Location ..
    }
}

cd ./frameworks
Execute-InSubDirectories

cd ../modules
Execute-InSubDirectories

cd ../projects
Execute-InSubDirectories


