Remover coisas/arquivos inúteis
Retestar com tudo zerado

Criar healthcheck no container do backend para esperar ele estar disponível





Users are required to upload a file when creating a company.
Only files with the following extensions are allowed: pdf, png, jpg ou jpeg.
If a file is uploaded with the wrong extension, the system will pop up a toast message "São válidos somente arquivos do tipo: pdf, png, jpg ou jpeg." with the following title: "Arquivo Inválido".
Users are not allowed to upload the same file twice (a hashing algorithm should be used to check for duplicates) globally across the system
If the user tries to register a company without uploading a file, the system will pop up a toast message "É necessário enviar os arquivos obrigatórios para prosseguir." with the following title: "Aviso".
If the user tries to upload the same file twice, the system will pop up a toast message "Arquivo duplicado" with the following title: "Arquivo duplicado".
The file should be acessible by the details page.
You should store this file in Postgres database.
Users can only send one file and it can be later updated  in the edit page.





Users can reprove the company if they are pending to be approved
