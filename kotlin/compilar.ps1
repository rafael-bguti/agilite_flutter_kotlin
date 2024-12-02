$Env:MAVEN_OPTS = "-Dfile.encoding=UTF-8"

$javaHome = "C:/Developer_Tools/java/jdk-21"
$pathAtual = $env:PATH

$Env:JAVA_HOME = $javaHome
$env:PATH = "$javaHome\bin;$pathAtual"

$mvnVersion = mvn -version
if ($mvnVersion -like "*Java version: 21*") {
    Write-Host $mvnVersion
}
else {
    Write-Host "`n********************************" -ForegroundColor Red
    Write-Host "******Java diferente de 21******" -ForegroundColor Red
    Write-Host "********************************`n" -ForegroundColor Red
    exit 1
}

mvn clean package -DskipTests
