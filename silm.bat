@echo off
rem Compila todos os arquivos .java usando a biblioteca DESMO-J
javac -cp desmoj-2.5.1e-bin.jar; *.java

rem Executa o programa principal (ClinicaVidaSilm)
java -cp desmoj-2.5.1e-bin.jar; ClinicaVidaSilm

rem Pausa para visualizar a saída antes de fechar
pause
