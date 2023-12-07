program	SPROGRAM	17	1
quicksort	SIDENTIFIER	43	1
;	SSEMICOLON	37	1
var	SVAR	21	2
v	SIDENTIFIER	43	2
:	SCOLON	38	2
array	SARRAY	1	2
[	SLBRACKET	35	2
1	SCONSTANT	44	2
..	SRANGE	39	2
100	SCONSTANT	44	2
]	SRBRACKET	36	2
of	SOF	14	2
integer	SINTEGER	11	2
;	SSEMICOLON	37	2
lv	SIDENTIFIER	43	3
:	SCOLON	38	3
array	SARRAY	1	3
[	SLBRACKET	35	3
1	SCONSTANT	44	3
..	SRANGE	39	3
20	SCONSTANT	44	3
]	SRBRACKET	36	3
of	SOF	14	3
integer	SINTEGER	11	3
;	SSEMICOLON	37	3
uv	SIDENTIFIER	43	4
:	SCOLON	38	4
array	SARRAY	1	4
[	SLBRACKET	35	4
1	SCONSTANT	44	4
..	SRANGE	39	4
20	SCONSTANT	44	4
]	SRBRACKET	36	4
of	SOF	14	4
integer	SINTEGER	11	4
;	SSEMICOLON	37	4
p	SIDENTIFIER	43	5
:	SCOLON	38	5
integer	SINTEGER	11	5
;	SSEMICOLON	37	5
pivlin	SIDENTIFIER	43	6
:	SCOLON	38	6
integer	SINTEGER	11	6
;	SSEMICOLON	37	6
temp	SIDENTIFIER	43	7
:	SCOLON	38	7
integer	SINTEGER	11	7
;	SSEMICOLON	37	7
i	SIDENTIFIER	43	8
:	SCOLON	38	8
integer	SINTEGER	11	8
;	SSEMICOLON	37	8
j	SIDENTIFIER	43	9
:	SCOLON	38	9
integer	SINTEGER	11	9
;	SSEMICOLON	37	9
n	SIDENTIFIER	43	10
:	SCOLON	38	10
integer	SINTEGER	11	10
;	SSEMICOLON	37	10
begin	SBEGIN	2	12
n	SIDENTIFIER	43	13
:=	SASSIGN	40	13
20	SCONSTANT	44	13
;	SSEMICOLON	37	13
writeln	SWRITELN	23	14
(	SLPAREN	33	14
'***** quick sort *****'	SSTRING	45	14
)	SRPAREN	34	14
;	SSEMICOLON	37	14
v	SIDENTIFIER	43	15
[	SLBRACKET	35	15
1	SCONSTANT	44	15
]	SRBRACKET	36	15
:=	SASSIGN	40	15
12	SCONSTANT	44	15
;	SSEMICOLON	37	15
i	SIDENTIFIER	43	16
:=	SASSIGN	40	16
2	SCONSTANT	44	16
;	SSEMICOLON	37	16
while	SWHILE	22	17
i	SIDENTIFIER	43	17
<=	SLESSEQUAL	27	17
n	SIDENTIFIER	43	17
do	SDO	6	17
begin	SBEGIN	2	18
v	SIDENTIFIER	43	19
[	SLBRACKET	35	19
i	SIDENTIFIER	43	19
]	SRBRACKET	36	19
:=	SASSIGN	40	19
v	SIDENTIFIER	43	19
[	SLBRACKET	35	19
i	SIDENTIFIER	43	19
-	SMINUS	31	19
1	SCONSTANT	44	19
]	SRBRACKET	36	19
*	SSTAR	32	19
65	SCONSTANT	44	19
+	SPLUS	30	19
17	SCONSTANT	44	19
;	SSEMICOLON	37	19
v	SIDENTIFIER	43	20
[	SLBRACKET	35	20
i	SIDENTIFIER	43	20
]	SRBRACKET	36	20
:=	SASSIGN	40	20
v	SIDENTIFIER	43	20
[	SLBRACKET	35	20
i	SIDENTIFIER	43	20
]	SRBRACKET	36	20
-	SMINUS	31	20
v	SIDENTIFIER	43	20
[	SLBRACKET	35	20
i	SIDENTIFIER	43	20
]	SRBRACKET	36	20
div	SDIVD	5	20
256	SCONSTANT	44	20
*	SSTAR	32	20
256	SCONSTANT	44	20
;	SSEMICOLON	37	20
i	SIDENTIFIER	43	21
:=	SASSIGN	40	21
i	SIDENTIFIER	43	21
+	SPLUS	30	21
1	SCONSTANT	44	21
;	SSEMICOLON	37	21
end	SEND	8	22
;	SSEMICOLON	37	22
writeln	SWRITELN	23	23
(	SLPAREN	33	23
'***** data ******'	SSTRING	45	23
)	SRPAREN	34	23
;	SSEMICOLON	37	23
i	SIDENTIFIER	43	24
:=	SASSIGN	40	24
1	SCONSTANT	44	24
;	SSEMICOLON	37	24
while	SWHILE	22	25
i	SIDENTIFIER	43	25
<=	SLESSEQUAL	27	25
n	SIDENTIFIER	43	25
do	SDO	6	25
begin	SBEGIN	2	26
writeln	SWRITELN	23	27
(	SLPAREN	33	27
v	SIDENTIFIER	43	27
[	SLBRACKET	35	27
i	SIDENTIFIER	43	27
]	SRBRACKET	36	27
,	SCOMMA	41	27
' '	SSTRING	45	27
,	SCOMMA	41	27
v	SIDENTIFIER	43	27
[	SLBRACKET	35	27
i	SIDENTIFIER	43	27
+	SPLUS	30	27
1	SCONSTANT	44	27
]	SRBRACKET	36	27
,	SCOMMA	41	27
' '	SSTRING	45	27
,	SCOMMA	41	27
v	SIDENTIFIER	43	27
[	SLBRACKET	35	27
i	SIDENTIFIER	43	27
+	SPLUS	30	27
2	SCONSTANT	44	27
]	SRBRACKET	36	27
,	SCOMMA	41	27
' '	SSTRING	45	27
,	SCOMMA	41	27
v	SIDENTIFIER	43	27
[	SLBRACKET	35	27
i	SIDENTIFIER	43	27
+	SPLUS	30	27
3	SCONSTANT	44	27
]	SRBRACKET	36	27
,	SCOMMA	41	27
' '	SSTRING	45	27
,	SCOMMA	41	27
v	SIDENTIFIER	43	27
[	SLBRACKET	35	27
i	SIDENTIFIER	43	27
+	SPLUS	30	27
4	SCONSTANT	44	27
]	SRBRACKET	36	27
)	SRPAREN	34	27
;	SSEMICOLON	37	27
i	SIDENTIFIER	43	28
:=	SASSIGN	40	28
i	SIDENTIFIER	43	28
+	SPLUS	30	28
5	SCONSTANT	44	28
;	SSEMICOLON	37	28
end	SEND	8	29
;	SSEMICOLON	37	29
lv	SIDENTIFIER	43	30
[	SLBRACKET	35	30
1	SCONSTANT	44	30
]	SRBRACKET	36	30
:=	SASSIGN	40	30
1	SCONSTANT	44	30
;	SSEMICOLON	37	30
uv	SIDENTIFIER	43	31
[	SLBRACKET	35	31
1	SCONSTANT	44	31
]	SRBRACKET	36	31
:=	SASSIGN	40	31
n	SIDENTIFIER	43	31
;	SSEMICOLON	37	31
p	SIDENTIFIER	43	32
:=	SASSIGN	40	32
1	SCONSTANT	44	32
;	SSEMICOLON	37	32
while	SWHILE	22	33
p	SIDENTIFIER	43	33
>	SGREAT	29	33
0	SCONSTANT	44	33
do	SDO	6	33
begin	SBEGIN	2	34
if	SIF	10	35
lv	SIDENTIFIER	43	35
[	SLBRACKET	35	35
p	SIDENTIFIER	43	35
]	SRBRACKET	36	35
>=	SGREATEQUAL	28	35
uv	SIDENTIFIER	43	35
[	SLBRACKET	35	35
p	SIDENTIFIER	43	35
]	SRBRACKET	36	35
then	STHEN	19	35
begin	SBEGIN	2	36
p	SIDENTIFIER	43	37
:=	SASSIGN	40	37
p	SIDENTIFIER	43	37
-	SMINUS	31	37
1	SCONSTANT	44	37
;	SSEMICOLON	37	37
end	SEND	8	38
else	SELSE	7	39
begin	SBEGIN	2	40
i	SIDENTIFIER	43	41
:=	SASSIGN	40	41
lv	SIDENTIFIER	43	41
[	SLBRACKET	35	41
p	SIDENTIFIER	43	41
]	SRBRACKET	36	41
-	SMINUS	31	41
1	SCONSTANT	44	41
;	SSEMICOLON	37	41
j	SIDENTIFIER	43	42
:=	SASSIGN	40	42
uv	SIDENTIFIER	43	42
[	SLBRACKET	35	42
p	SIDENTIFIER	43	42
]	SRBRACKET	36	42
;	SSEMICOLON	37	42
pivlin	SIDENTIFIER	43	43
:=	SASSIGN	40	43
v	SIDENTIFIER	43	43
[	SLBRACKET	35	43
j	SIDENTIFIER	43	43
]	SRBRACKET	36	43
;	SSEMICOLON	37	43
while	SWHILE	22	44
i	SIDENTIFIER	43	44
<	SLESS	26	44
j	SIDENTIFIER	43	44
do	SDO	6	44
begin	SBEGIN	2	45
i	SIDENTIFIER	43	46
:=	SASSIGN	40	46
i	SIDENTIFIER	43	46
+	SPLUS	30	46
1	SCONSTANT	44	46
;	SSEMICOLON	37	46
while	SWHILE	22	47
v	SIDENTIFIER	43	47
[	SLBRACKET	35	47
i	SIDENTIFIER	43	47
]	SRBRACKET	36	47
<	SLESS	26	47
pivlin	SIDENTIFIER	43	47
do	SDO	6	47
begin	SBEGIN	2	48
i	SIDENTIFIER	43	49
:=	SASSIGN	40	49
i	SIDENTIFIER	43	49
+	SPLUS	30	49
1	SCONSTANT	44	49
;	SSEMICOLON	37	49
end	SEND	8	50
;	SSEMICOLON	37	50
j	SIDENTIFIER	43	51
:=	SASSIGN	40	51
j	SIDENTIFIER	43	51
-	SMINUS	31	51
1	SCONSTANT	44	51
;	SSEMICOLON	37	51
while	SWHILE	22	52
v	SIDENTIFIER	43	52
[	SLBRACKET	35	52
j	SIDENTIFIER	43	52
]	SRBRACKET	36	52
>	SGREAT	29	52
pivlin	SIDENTIFIER	43	52
do	SDO	6	52
begin	SBEGIN	2	53
j	SIDENTIFIER	43	54
:=	SASSIGN	40	54
j	SIDENTIFIER	43	54
-	SMINUS	31	54
1	SCONSTANT	44	54
;	SSEMICOLON	37	54
if	SIF	10	55
i	SIDENTIFIER	43	55
>=	SGREATEQUAL	28	55
j	SIDENTIFIER	43	55
then	STHEN	19	55
begin	SBEGIN	2	56
pivlin	SIDENTIFIER	43	57
:=	SASSIGN	40	57
v	SIDENTIFIER	43	57
[	SLBRACKET	35	57
j	SIDENTIFIER	43	57
]	SRBRACKET	36	57
;	SSEMICOLON	37	57
end	SEND	8	58
;	SSEMICOLON	37	58
end	SEND	8	59
;	SSEMICOLON	37	59
if	SIF	10	60
i	SIDENTIFIER	43	60
<	SLESS	26	60
j	SIDENTIFIER	43	60
then	STHEN	19	60
begin	SBEGIN	2	61
temp	SIDENTIFIER	43	62
:=	SASSIGN	40	62
v	SIDENTIFIER	43	62
[	SLBRACKET	35	62
i	SIDENTIFIER	43	62
]	SRBRACKET	36	62
;	SSEMICOLON	37	62
v	SIDENTIFIER	43	63
[	SLBRACKET	35	63
i	SIDENTIFIER	43	63
]	SRBRACKET	36	63
:=	SASSIGN	40	63
v	SIDENTIFIER	43	63
[	SLBRACKET	35	63
j	SIDENTIFIER	43	63
]	SRBRACKET	36	63
;	SSEMICOLON	37	63
v	SIDENTIFIER	43	64
[	SLBRACKET	35	64
j	SIDENTIFIER	43	64
]	SRBRACKET	36	64
:=	SASSIGN	40	64
temp	SIDENTIFIER	43	64
;	SSEMICOLON	37	64
end	SEND	8	65
;	SSEMICOLON	37	65
end	SEND	8	66
;	SSEMICOLON	37	66
temp	SIDENTIFIER	43	67
:=	SASSIGN	40	67
v	SIDENTIFIER	43	67
[	SLBRACKET	35	67
i	SIDENTIFIER	43	67
]	SRBRACKET	36	67
;	SSEMICOLON	37	67
v	SIDENTIFIER	43	68
[	SLBRACKET	35	68
i	SIDENTIFIER	43	68
]	SRBRACKET	36	68
:=	SASSIGN	40	68
v	SIDENTIFIER	43	68
[	SLBRACKET	35	68
uv	SIDENTIFIER	43	68
[	SLBRACKET	35	68
p	SIDENTIFIER	43	68
]	SRBRACKET	36	68
]	SRBRACKET	36	68
;	SSEMICOLON	37	68
v	SIDENTIFIER	43	69
[	SLBRACKET	35	69
uv	SIDENTIFIER	43	69
[	SLBRACKET	35	69
p	SIDENTIFIER	43	69
]	SRBRACKET	36	69
]	SRBRACKET	36	69
:=	SASSIGN	40	69
temp	SIDENTIFIER	43	69
;	SSEMICOLON	37	69
if	SIF	10	70
i	SIDENTIFIER	43	70
-	SMINUS	31	70
lv	SIDENTIFIER	43	70
[	SLBRACKET	35	70
p	SIDENTIFIER	43	70
]	SRBRACKET	36	70
<	SLESS	26	70
uv	SIDENTIFIER	43	70
[	SLBRACKET	35	70
p	SIDENTIFIER	43	70
]	SRBRACKET	36	70
-	SMINUS	31	70
i	SIDENTIFIER	43	70
then	STHEN	19	70
begin	SBEGIN	2	71
lv	SIDENTIFIER	43	72
[	SLBRACKET	35	72
p	SIDENTIFIER	43	72
+	SPLUS	30	72
1	SCONSTANT	44	72
]	SRBRACKET	36	72
:=	SASSIGN	40	72
lv	SIDENTIFIER	43	72
[	SLBRACKET	35	72
p	SIDENTIFIER	43	72
]	SRBRACKET	36	72
;	SSEMICOLON	37	72
uv	SIDENTIFIER	43	73
[	SLBRACKET	35	73
p	SIDENTIFIER	43	73
+	SPLUS	30	73
1	SCONSTANT	44	73
]	SRBRACKET	36	73
:=	SASSIGN	40	73
i	SIDENTIFIER	43	73
-	SMINUS	31	73
1	SCONSTANT	44	73
;	SSEMICOLON	37	73
lv	SIDENTIFIER	43	74
[	SLBRACKET	35	74
p	SIDENTIFIER	43	74
]	SRBRACKET	36	74
:=	SASSIGN	40	74
i	SIDENTIFIER	43	74
+	SPLUS	30	74
1	SCONSTANT	44	74
;	SSEMICOLON	37	74
end	SEND	8	75
else	SELSE	7	76
begin	SBEGIN	2	77
lv	SIDENTIFIER	43	78
[	SLBRACKET	35	78
p	SIDENTIFIER	43	78
+	SPLUS	30	78
1	SCONSTANT	44	78
]	SRBRACKET	36	78
:=	SASSIGN	40	78
i	SIDENTIFIER	43	78
+	SPLUS	30	78
1	SCONSTANT	44	78
;	SSEMICOLON	37	78
uv	SIDENTIFIER	43	79
[	SLBRACKET	35	79
p	SIDENTIFIER	43	79
+	SPLUS	30	79
1	SCONSTANT	44	79
]	SRBRACKET	36	79
:=	SASSIGN	40	79
uv	SIDENTIFIER	43	79
[	SLBRACKET	35	79
p	SIDENTIFIER	43	79
]	SRBRACKET	36	79
;	SSEMICOLON	37	79
uv	SIDENTIFIER	43	80
[	SLBRACKET	35	80
p	SIDENTIFIER	43	80
]	SRBRACKET	36	80
:=	SASSIGN	40	80
i	SIDENTIFIER	43	80
-	SMINUS	31	80
1	SCONSTANT	44	80
;	SSEMICOLON	37	80
end	SEND	8	81
;	SSEMICOLON	37	81
p	SIDENTIFIER	43	82
:=	SASSIGN	40	82
p	SIDENTIFIER	43	82
+	SPLUS	30	82
1	SCONSTANT	44	82
;	SSEMICOLON	37	82
end	SEND	8	83
;	SSEMICOLON	37	83
end	SEND	8	84
;	SSEMICOLON	37	84
writeln	SWRITELN	23	85
(	SLPAREN	33	85
'***** result ******'	SSTRING	45	85
)	SRPAREN	34	85
;	SSEMICOLON	37	85
i	SIDENTIFIER	43	86
:=	SASSIGN	40	86
1	SCONSTANT	44	86
;	SSEMICOLON	37	86
while	SWHILE	22	87
i	SIDENTIFIER	43	87
<=	SLESSEQUAL	27	87
n	SIDENTIFIER	43	87
do	SDO	6	87
begin	SBEGIN	2	88
writeln	SWRITELN	23	89
(	SLPAREN	33	89
v	SIDENTIFIER	43	89
[	SLBRACKET	35	89
i	SIDENTIFIER	43	89
]	SRBRACKET	36	89
,	SCOMMA	41	89
' '	SSTRING	45	89
,	SCOMMA	41	89
v	SIDENTIFIER	43	89
[	SLBRACKET	35	89
i	SIDENTIFIER	43	89
+	SPLUS	30	89
1	SCONSTANT	44	89
]	SRBRACKET	36	89
,	SCOMMA	41	89
' '	SSTRING	45	89
,	SCOMMA	41	89
v	SIDENTIFIER	43	89
[	SLBRACKET	35	89
i	SIDENTIFIER	43	89
+	SPLUS	30	89
2	SCONSTANT	44	89
]	SRBRACKET	36	89
,	SCOMMA	41	89
' '	SSTRING	45	89
,	SCOMMA	41	89
v	SIDENTIFIER	43	89
[	SLBRACKET	35	89
i	SIDENTIFIER	43	89
+	SPLUS	30	89
3	SCONSTANT	44	89
]	SRBRACKET	36	89
,	SCOMMA	41	89
' '	SSTRING	45	89
,	SCOMMA	41	89
v	SIDENTIFIER	43	89
[	SLBRACKET	35	89
i	SIDENTIFIER	43	89
+	SPLUS	30	89
4	SCONSTANT	44	89
]	SRBRACKET	36	89
)	SRPAREN	34	89
;	SSEMICOLON	37	89
i	SIDENTIFIER	43	90
:=	SASSIGN	40	90
i	SIDENTIFIER	43	90
+	SPLUS	30	90
5	SCONSTANT	44	90
;	SSEMICOLON	37	90
end	SEND	8	91
;	SSEMICOLON	37	91
end	SEND	8	92
.	SDOT	42	92
