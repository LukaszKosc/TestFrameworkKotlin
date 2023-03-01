java -classpath . -jar .\libs\appTest-1.0-SNAPSHOT.jar

java -classpath . -jar .\libs\appMain-1.0-SNAPSHOT.jar

java -jar .\libs\TestFramework-1.0-SNAPSHOT.jar

Get-ChildItem .\build\libs -Filter *.jar | Copy-Item -Destination .\libs\ -Force -PassThru