all:
	gcc -o printserverd printserver.c -Wall

install:
	cp printserverd /usr/bin/printserverd
	cp printserver /etc/init.d/printserver
	update-rc.d printserver defaults
	/etc/init.d/printserver start

clean:
	/etc/init.d/printserver stop
	update-rc.d printserver remove
	rm /usr/bin/printserverd
	rm /etc/init.d/printserver