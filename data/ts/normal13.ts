program	SPROGRAM	17	1
primeFactorization	SIDENTIFIER	43	1
;	SSEMICOLON	37	1
var	SVAR	21	2
FACTOR	SIDENTIFIER	43	2
:	SCOLON	38	2
array	SARRAY	1	2
[	SLBRACKET	35	2
1	SCONSTANT	44	2
..	SRANGE	39	2
30	SCONSTANT	44	2
]	SRBRACKET	36	2
of	SOF	14	2
integer	SINTEGER	11	2
;	SSEMICOLON	37	2
TIMES	SIDENTIFIER	43	3
:	SCOLON	38	3
array	SARRAY	1	3
[	SLBRACKET	35	3
1	SCONSTANT	44	3
..	SRANGE	39	3
30	SCONSTANT	44	3
]	SRBRACKET	36	3
of	SOF	14	3
integer	SINTEGER	11	3
;	SSEMICOLON	37	3
DATA	SIDENTIFIER	43	4
:	SCOLON	38	4
array	SARRAY	1	4
[	SLBRACKET	35	4
1	SCONSTANT	44	4
..	SRANGE	39	4
10	SCONSTANT	44	4
]	SRBRACKET	36	4
of	SOF	14	4
integer	SINTEGER	11	4
;	SSEMICOLON	37	4
NUM	SIDENTIFIER	43	5
:	SCOLON	38	5
integer	SINTEGER	11	5
;	SSEMICOLON	37	5
i	SIDENTIFIER	43	6
,	SCOMMA	41	6
j	SIDENTIFIER	43	6
,	SCOMMA	41	6
k	SIDENTIFIER	43	6
:	SCOLON	38	6
integer	SINTEGER	11	6
;	SSEMICOLON	37	6
m	SIDENTIFIER	43	7
,	SCOMMA	41	7
n	SIDENTIFIER	43	7
:	SCOLON	38	7
integer	SINTEGER	11	7
;	SSEMICOLON	37	7
begin	SBEGIN	2	8
m	SIDENTIFIER	43	9
:=	SASSIGN	40	9
17	SCONSTANT	44	9
;	SSEMICOLON	37	9
n	SIDENTIFIER	43	10
:=	SASSIGN	40	10
23	SCONSTANT	44	10
;	SSEMICOLON	37	10
DATA	SIDENTIFIER	43	11
[	SLBRACKET	35	11
1	SCONSTANT	44	11
]	SRBRACKET	36	11
:=	SASSIGN	40	11
345	SCONSTANT	44	11
;	SSEMICOLON	37	11
i	SIDENTIFIER	43	12
:=	SASSIGN	40	12
2	SCONSTANT	44	12
;	SSEMICOLON	37	12
while	SWHILE	22	13
i	SIDENTIFIER	43	13
<=	SLESSEQUAL	27	13
10	SCONSTANT	44	13
do	SDO	6	13
begin	SBEGIN	2	14
DATA	SIDENTIFIER	43	15
[	SLBRACKET	35	15
i	SIDENTIFIER	43	15
]	SRBRACKET	36	15
:=	SASSIGN	40	15
DATA	SIDENTIFIER	43	15
[	SLBRACKET	35	15
i	SIDENTIFIER	43	15
-	SMINUS	31	15
1	SCONSTANT	44	15
]	SRBRACKET	36	15
*	SSTAR	32	15
m	SIDENTIFIER	43	15
+	SPLUS	30	15
n	SIDENTIFIER	43	15
;	SSEMICOLON	37	15
while	SWHILE	22	16
DATA	SIDENTIFIER	43	16
[	SLBRACKET	35	16
i	SIDENTIFIER	43	16
]	SRBRACKET	36	16
>	SGREAT	29	16
1000	SCONSTANT	44	16
do	SDO	6	16
begin	SBEGIN	2	17
DATA	SIDENTIFIER	43	18
[	SLBRACKET	35	18
i	SIDENTIFIER	43	18
]	SRBRACKET	36	18
:=	SASSIGN	40	18
DATA	SIDENTIFIER	43	18
[	SLBRACKET	35	18
i	SIDENTIFIER	43	18
]	SRBRACKET	36	18
-	SMINUS	31	18
1000	SCONSTANT	44	18
;	SSEMICOLON	37	18
end	SEND	8	19
;	SSEMICOLON	37	19
i	SIDENTIFIER	43	20
:=	SASSIGN	40	20
i	SIDENTIFIER	43	20
+	SPLUS	30	20
1	SCONSTANT	44	20
;	SSEMICOLON	37	20
end	SEND	8	21
;	SSEMICOLON	37	21
k	SIDENTIFIER	43	22
:=	SASSIGN	40	22
1	SCONSTANT	44	22
;	SSEMICOLON	37	22
while	SWHILE	22	23
k	SIDENTIFIER	43	23
<=	SLESSEQUAL	27	23
10	SCONSTANT	44	23
do	SDO	6	23
begin	SBEGIN	2	24
j	SIDENTIFIER	43	25
:=	SASSIGN	40	25
2	SCONSTANT	44	25
;	SSEMICOLON	37	25
i	SIDENTIFIER	43	26
:=	SASSIGN	40	26
0	SCONSTANT	44	26
;	SSEMICOLON	37	26
NUM	SIDENTIFIER	43	27
:=	SASSIGN	40	27
DATA	SIDENTIFIER	43	27
[	SLBRACKET	35	27
k	SIDENTIFIER	43	27
]	SRBRACKET	36	27
;	SSEMICOLON	37	27
while	SWHILE	22	28
NUM	SIDENTIFIER	43	28
<>	SNOTEQUAL	25	28
1	SCONSTANT	44	28
do	SDO	6	28
begin	SBEGIN	2	29
if	SIF	10	30
NUM	SIDENTIFIER	43	30
div	SDIVD	5	30
j	SIDENTIFIER	43	30
*	SSTAR	32	30
j	SIDENTIFIER	43	30
=	SEQUAL	24	30
NUM	SIDENTIFIER	43	30
then	STHEN	19	30
begin	SBEGIN	2	31
i	SIDENTIFIER	43	32
:=	SASSIGN	40	32
i	SIDENTIFIER	43	32
+	SPLUS	30	32
1	SCONSTANT	44	32
;	SSEMICOLON	37	32
FACTOR	SIDENTIFIER	43	33
[	SLBRACKET	35	33
i	SIDENTIFIER	43	33
]	SRBRACKET	36	33
:=	SASSIGN	40	33
j	SIDENTIFIER	43	33
;	SSEMICOLON	37	33
TIMES	SIDENTIFIER	43	34
[	SLBRACKET	35	34
i	SIDENTIFIER	43	34
]	SRBRACKET	36	34
:=	SASSIGN	40	34
1	SCONSTANT	44	34
;	SSEMICOLON	37	34
NUM	SIDENTIFIER	43	35
:=	SASSIGN	40	35
NUM	SIDENTIFIER	43	35
div	SDIVD	5	35
j	SIDENTIFIER	43	35
;	SSEMICOLON	37	35
end	SEND	8	36
;	SSEMICOLON	37	36
while	SWHILE	22	37
NUM	SIDENTIFIER	43	37
div	SDIVD	5	37
j	SIDENTIFIER	43	37
*	SSTAR	32	37
j	SIDENTIFIER	43	37
=	SEQUAL	24	37
NUM	SIDENTIFIER	43	37
do	SDO	6	37
begin	SBEGIN	2	38
TIMES	SIDENTIFIER	43	39
[	SLBRACKET	35	39
i	SIDENTIFIER	43	39
]	SRBRACKET	36	39
:=	SASSIGN	40	39
TIMES	SIDENTIFIER	43	39
[	SLBRACKET	35	39
i	SIDENTIFIER	43	39
]	SRBRACKET	36	39
+	SPLUS	30	39
1	SCONSTANT	44	39
;	SSEMICOLON	37	39
NUM	SIDENTIFIER	43	40
:=	SASSIGN	40	40
NUM	SIDENTIFIER	43	40
div	SDIVD	5	40
j	SIDENTIFIER	43	40
;	SSEMICOLON	37	40
end	SEND	8	41
;	SSEMICOLON	37	41
if	SIF	10	42
j	SIDENTIFIER	43	42
=	SEQUAL	24	42
2	SCONSTANT	44	42
then	STHEN	19	42
begin	SBEGIN	2	43
j	SIDENTIFIER	43	44
:=	SASSIGN	40	44
3	SCONSTANT	44	44
;	SSEMICOLON	37	44
end	SEND	8	45
else	SELSE	7	46
begin	SBEGIN	2	47
j	SIDENTIFIER	43	48
:=	SASSIGN	40	48
j	SIDENTIFIER	43	48
+	SPLUS	30	48
2	SCONSTANT	44	48
;	SSEMICOLON	37	48
end	SEND	8	49
;	SSEMICOLON	37	49
end	SEND	8	50
;	SSEMICOLON	37	50
j	SIDENTIFIER	43	51
:=	SASSIGN	40	51
1	SCONSTANT	44	51
;	SSEMICOLON	37	51
writeln	SWRITELN	23	52
(	SLPAREN	33	52
' ANARISE '	SSTRING	45	52
,	SCOMMA	41	52
DATA	SIDENTIFIER	43	52
[	SLBRACKET	35	52
k	SIDENTIFIER	43	52
]	SRBRACKET	36	52
,	SCOMMA	41	52
' = '	SSTRING	45	52
)	SRPAREN	34	52
;	SSEMICOLON	37	52
while	SWHILE	22	53
j	SIDENTIFIER	43	53
<=	SLESSEQUAL	27	53
i	SIDENTIFIER	43	53
do	SDO	6	53
begin	SBEGIN	2	54
writeln	SWRITELN	23	55
(	SLPAREN	33	55
'               '	SSTRING	45	55
,	SCOMMA	41	55
FACTOR	SIDENTIFIER	43	55
[	SLBRACKET	35	55
j	SIDENTIFIER	43	55
]	SRBRACKET	36	55
,	SCOMMA	41	55
'^'	SSTRING	45	55
,	SCOMMA	41	55
TIMES	SIDENTIFIER	43	55
[	SLBRACKET	35	55
j	SIDENTIFIER	43	55
]	SRBRACKET	36	55
)	SRPAREN	34	55
;	SSEMICOLON	37	55
j	SIDENTIFIER	43	56
:=	SASSIGN	40	56
j	SIDENTIFIER	43	56
+	SPLUS	30	56
1	SCONSTANT	44	56
;	SSEMICOLON	37	56
end	SEND	8	57
;	SSEMICOLON	37	57
k	SIDENTIFIER	43	58
:=	SASSIGN	40	58
k	SIDENTIFIER	43	58
+	SPLUS	30	58
1	SCONSTANT	44	58
;	SSEMICOLON	37	58
end	SEND	8	59
;	SSEMICOLON	37	59
end	SEND	8	60
.	SDOT	42	60
