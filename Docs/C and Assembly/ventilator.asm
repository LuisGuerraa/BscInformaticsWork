.section start
.org 0
jmp main
jmp interrupt

led:
.word 0xff00

timer:
.word 0xff40
 
fim:
.word 50

tempoMax:
.word 120

guardartempoOn:
.word 70

guardartempototal:
.word 80

main:
     ldi r0,#0                       ;tempo inicial começa com 0
     st r0,guardartempoOn            ;guardar o tempoOn na memória

     ldi r0,#0                       ;tempo inicial começa com 0
     st r0,guardartempototal         ;guardar o tempo Total na memória

     ldi r0,#0                       ;variavel que vai avisar o interrupt que o tempo final chegou a 0
     st r0,fim                       ;guardar na memória

     ld r0,led                       ;fazer reset do led do switch utilizando o endereço 0xff00
     ldi r1,#0
     stb r1,[r0,#1]
                            
     ldi r0,#0                       ;colocar o valor 0 no timer
     ld r1,timer
     ldb r0,[r1,#1]                  ;reset do flip-flop 
     stb r0,[r1,#1]
    
     
    botao_down:                      ;à espera da ativação do botao On 
              ld r0,led
              ldb r1,[r0,#1]
              sub r1,r1,#1
              jz acender_led
              jmp botao_down


    acender_led:                     ;acender a FAN
              ldi r1,#1
              stb r1,[r0,#1]
              ld r1,timer
              ldi r0,#0
              ldb r2,[r1,#1]         ;reset do flip-flop 
              stb r0,[r1,#1]         ;colocar o tempo a 0 no timer
              ldi r0,#10             ;a cada 1 segundo vai para a interrupção
              stb r0,[r1,#1]         ;guardar tempo no timer

    On_ligado:     
              ld r0,led              ;acender o led outra vez   
              ldi r1,#1
              stb r1,[r0,#1]
              ldi r6,#16             ;verificar se aconteceu a interrupção
              ld r2,led              ;ver se o botao ON está desligado
              ldb r3,[R2,#1]
              sub R3,R3,#0
              jz obterTempoOn        
              jmp On_ligado


    obterTempoOn:                     	      ;função que obtem o tempo em que o forno estava ligado
              LDi R6,#0                       ;impedir que o programa volte a entrar na interrupção
              ld r2,timer                           
              LDB r3,[r2,#1]                  ;fazer reset do flip flop
              ld r4,guardartempoOn            ;buscar o tempo que o forno esteve ligado
	      ld r3,guardartempototal         ;buscar o tempo total
	      add r3,r3,r4                    ;somar o tempo que o forno esteve ligado com o tempo total(no inicio começa com zero)
	      st r3,guardartempototal         ;guardar o tempo total na memória  
	      ldi r4,#0                       ;resetar o tempo do forno
	      st r4,guardartempoOn

    IniciarTimer:                             ;função que inicia a contagem decrescente do ventilador com o tempo total
              ld  r0,led
              ldi r1,#1
              stb r1,[r0,#1]
              ld r1,timer
              ldi r0,#0
              ldb r2,[r1,#1]                  ;reset do flip-flop 
              stb r0,[r1,#1]                  ;colocar o tempo a 0 no timer
              ldi r0,#10                      ;a cada 1 segundo vai para a interrupção
              stb r0,[r1,#1]                  ;guardar tempo no timer

    wait_timer:                               ;á espera do tempo do ventilador chegar a zero,caso o forno seja ligado neste periodo, é adicionado o tempo do forno á contagem decrescente
              LDi r3,#0
              add r3,r3,#1
              st r3,fim
              ld r0,led                       ;acender o led outra vez   
              LDB r1,[r0,#1]
              sub r3,r1,#1
              jz acender_led
              ldi r6,#16
              jmp wait_timer

interrupt:
        ld r2,led                             ;ver se o botao ON está desligado
        ldb r3,[R2,#1]
        sub R3,R3,#1
        jz incrementarTempoOn      	
        ld r2,guardartempototal               ;caso o forno estiver desligado o tempo total é decrementado
        sub r2,r2,#10
        st r2,guardartempototal
        sub r2,r2,#0
        jz apagar_led                         ;se o tempo total chegar a 0 o ventilador é desligado
        ld r2,timer                          
        LDB r3,[r2,#1]	
        iret		
   
	incrementarTempoOn:                       ;se o forno estiver ligado inicia a contagem crescente da mesma
			ld r2,guardartempoOn
               		add r2,r2,#10
               		st r2,guardartempoOn      ;guardar o tempo que o forno esteve ligado na memória
			ld r2,timer
              		LDB r3,[r2,#1]            ;resetar o flip flop
			iret
			   
	apagar_led:                               ;o ventilador é desligado e o programa acaba
		      	ld r2,led          
			ldi r4,#0
              		stb r4,[R2,#1]
			ld r2,timer
              		LDB r3,[r2,#1]
                        jmp $
			iret		
		
.end