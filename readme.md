
# Instruções para rodar o projeto:

## Configurações do projeto:

### 1: Clonar o repositório
### 2: Na pasta base, mudar para a branch develop:
 - git checkout develop
 - git pull
### 3: No intelliJ Idea, ao lado do botão "Run", abrir o dropdown e selecionar "Edit configurations"
 - Certificar-se que o SDK selecionado é da versão 17 do Java
 - Configurar as variáveis de ambiente de acordo com os dados do banco, separadas por ponto e vírgula:
   - DB_URL=postgresql://{URL do banco}
   - DB_USERNAME={Nome do banco}
   - DB_PASSWORD={Senha do banco}

## Configurações do banco:

### 1: Na própria pasta do projeto tem um script SQL chamado data.sql. 
 - Copiar o script e colar num pgAdmin ou console do postgres, e rodar.
 - Será criado o banco com o nome 'porteiro'

### 2: As tabelas serão criadas automaticamente quando o projeto subir.