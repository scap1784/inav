CC=g++
OBJS=main_whois.o ../whois.o ../network.o
DEADLOCKOBJS=../mutex.o deadlock.o
LDFLAGS=-lpthread
CXXFLAGS=-pthread
#Make the test code for whois!
test_whois: ${OBJS}
	${CC} -o test_whois ${OBJS}

deadlock: ${DEADLOCKOBJS}
	${CC} $(LDFLAGS) -o deadlock ${DEADLOCKOBJS}

clean: 
	$(RM) *.o test_whois deadlock
	
