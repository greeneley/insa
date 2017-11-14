all: norandom

norandom: vulnerable_format_string
	sudo sh -c "echo 0 > /proc/sys/kernel/randomize_va_space"

random: vulnerable_format_string
	sudo sh -c "echo 1 > /proc/sys/kernel/randomize_va_space"

vulnerable_format_string: vulnerable_format_string.c
	gcc -Wall -m32 -fno-stack-protector vulnerable_format_string.c -o vulnerable_format_string

clean:
	rm -f *~ vulnerable_format_string

