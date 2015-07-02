# Baixa todos os arquivos de repasse de 2011 até 2015
# sugerido por Kaiser (alpkaiser@gmail.com)
for i in $(seq 2011 2015); do 
	for j in $(seq 1 9); do 
		wget -c "http://arquivos.portaldatransparencia.gov.br/downloads.asp?a=$i&m=0$j&consulta=Transferencias"; 
	done; 
	for j in $(seq 10 12); do 
		wget -c "http://arquivos.portaldatransparencia.gov.br/downloads.asp?a=$i&m=$j&consulta=Transferencias"; 
	done; 
done
