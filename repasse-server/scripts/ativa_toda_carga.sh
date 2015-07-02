# Baixa todos os arquivos de repasse de 2011 até 2015
# sugerido por Kaiser (alpkaiser@gmail.com)
for i in $(seq 2011 2015); do 
	for j in $(seq 1 12); do 
		curl -X POST http://localhost:8080/transferencias-governo-federal/rest/carga/$i/$j
	done; 
done
