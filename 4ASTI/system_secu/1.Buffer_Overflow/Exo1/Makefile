all: exploit vulnerable
	sudo sh -c "echo 0 > /proc/sys/kernel/randomize_va_space"

exploit: exploit.c
	gcc -g -Wall -m32 exploit.c -o exploit

vulnerable: vulnerable.c
	gcc -g -Wall -m32 -fno-stack-protector vulnerable.c -o vulnerable

clean:
	rm -f *~ exploit vulnerable
