# n39hacker's awesome search engine
## How to use

This project uses Apache Maven. In order to run it, you can generate project
files for your favourite IDE (e. g. Eclipse or IntelliJ) or just use
the command line like this:

`mvn exec:java -Dexec.mainClass="de.n39hackers.ir.REPL"`

That puts you into our REPL (read-eval-print loop). It is the main user
interface element and hopefully self-explanatory. The output will look like

```
Welcome to n39hacker's awesome search engine!

Your options are:
[0] Quit
[1] Print help
[2] Build index
[3] Select index
[4] Query
Current index: none
Available indices: 
```

Use 2 to build the first index. You can build an index for a single field or
for all of them. Just choose one of the options.

```
>> 2
[0] Use Title
[1] Use Date
[2] Use Body
[3] All
>> 3
```

When this is done you'll back in the main loop and can then choose which index
you'd like to use for querying. Type in 3 (Select index) and then choose from
the list of indices.

```
Your options are:
[0] Quit
[1] Print help
[2] Build index
[3] Select index
[4] Query
Current index: none
Available indices: 
[0] { date title body }

>> 3
Current index: none
Available indices: 
[0] { date title body }

0
Your options are:
[0] Quit
[1] Print help
[2] Build index
[3] Select index
[4] Query
Current index: { date title body }
Available indices: 
[0] { date title body }
```

Back in the main loop you can enter 4 and then query for whatever you like. You
will be asked how many of the top N results you'd like to see.

```
>> 4
query >> A*
Using search query "A*"
Please enter the number of results you'd like to see:
>> 5 
5 matches:
rank	id	score	title
0		5	1.15	NATIONAL AVERAGE PRICES FOR FARMER-OWNED RESERVE
1		6	1.15	ARGENTINE 1986/87 GRAIN/OILSEED REGISTRATIONS
2		9	1.15	CHAMPION PRODUCTS <CH> APPROVES STOCK SPLIT
3		13	1.15	AM INTERNATIONAL INC <AM> 2ND QTR JAN 31
4		39	1.15	DU PONT CO <DD> LAUNCHES IMPROVED ARAMID FIBERS
```

To quit the program type 0 in the main loop.
