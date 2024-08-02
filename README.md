# L4. Questão aberta

No caso de transações simultâneas, podemos criar um serviço para armazenar um semáforo em memória.
Este serviço possuirá um endpoint "/acquire/{id}" para sinalizar o início de um transação, sendo ID o ID da conta que a transação será realizada. Este serviço deve armazenar em memória todas as contas que estão bloqueadas pelo semáforo, devolvendo neste endpoint um código de resposta para sinalizar que a transação pode seguir ou que está bloqueada.
Caso a conta esteja bloqueada, o serviço de autorização deve imediamente retornar um erro na respostas. Caso contrário, ele pode seguir com o processamento, e ao finalizar, disparar uma requisição para um endpoint de liberação do semáfaro "/release/{id}".
É importante também implementar um timeout para transações bloqueadas, uma vez que pode ocorrer uma falha no autorizador, impedindo a chamada ao endpoint "release".