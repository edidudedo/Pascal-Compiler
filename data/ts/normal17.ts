program	SPROGRAM	17	1
testGcd	SIDENTIFIER	43	1
;	SSEMICOLON	37	1
var	SVAR	21	2
x	SIDENTIFIER	43	2
,	SCOMMA	41	2
y	SIDENTIFIER	43	2
,	SCOMMA	41	2
result	SIDENTIFIER	43	2
:	SCOLON	38	2
integer	SINTEGER	11	2
;	SSEMICOLON	37	2
procedure	SPROCEDURE	16	4
gcd	SIDENTIFIER	43	4
(	SLPAREN	33	4
x	SIDENTIFIER	43	4
,	SCOMMA	41	4
y	SIDENTIFIER	43	4
:	SCOLON	38	4
integer	SINTEGER	11	4
)	SRPAREN	34	4
;	SSEMICOLON	37	4
var	SVAR	21	5
nextY	SIDENTIFIER	43	5
,	SCOMMA	41	5
tmp	SIDENTIFIER	43	5
:	SCOLON	38	5
integer	SINTEGER	11	5
;	SSEMICOLON	37	5
begin	SBEGIN	2	6
if	SIF	10	7
x	SIDENTIFIER	43	7
<	SLESS	26	7
y	SIDENTIFIER	43	7
then	STHEN	19	7
begin	SBEGIN	2	8
tmp	SIDENTIFIER	43	9
:=	SASSIGN	40	9
x	SIDENTIFIER	43	9
;	SSEMICOLON	37	9
x	SIDENTIFIER	43	10
:=	SASSIGN	40	10
y	SIDENTIFIER	43	10
;	SSEMICOLON	37	10
y	SIDENTIFIER	43	11
:=	SASSIGN	40	11
tmp	SIDENTIFIER	43	11
;	SSEMICOLON	37	11
end	SEND	8	12
;	SSEMICOLON	37	12
if	SIF	10	13
y	SIDENTIFIER	43	13
=	SEQUAL	24	13
0	SCONSTANT	44	13
then	STHEN	19	13
begin	SBEGIN	2	14
result	SIDENTIFIER	43	15
:=	SASSIGN	40	15
x	SIDENTIFIER	43	15
;	SSEMICOLON	37	15
end	SEND	8	16
else	SELSE	7	17
begin	SBEGIN	2	18
nextY	SIDENTIFIER	43	19
:=	SASSIGN	40	19
x	SIDENTIFIER	43	19
mod	SMOD	12	19
y	SIDENTIFIER	43	19
;	SSEMICOLON	37	19
writeln	SWRITELN	23	20
(	SLPAREN	33	20
'  = gcd('	SSTRING	45	20
,	SCOMMA	41	20
y	SIDENTIFIER	43	20
,	SCOMMA	41	20
', '	SSTRING	45	20
,	SCOMMA	41	20
nextY	SIDENTIFIER	43	20
,	SCOMMA	41	20
')'	SSTRING	45	20
)	SRPAREN	34	20
;	SSEMICOLON	37	20
gcd	SIDENTIFIER	43	21
(	SLPAREN	33	21
y	SIDENTIFIER	43	21
,	SCOMMA	41	21
nextY	SIDENTIFIER	43	21
)	SRPAREN	34	21
;	SSEMICOLON	37	21
end	SEND	8	22
;	SSEMICOLON	37	22
end	SEND	8	23
;	SSEMICOLON	37	23
begin	SBEGIN	2	25
x	SIDENTIFIER	43	26
:=	SASSIGN	40	26
36	SCONSTANT	44	26
;	SSEMICOLON	37	26
y	SIDENTIFIER	43	27
:=	SASSIGN	40	27
24	SCONSTANT	44	27
;	SSEMICOLON	37	27
writeln	SWRITELN	23	28
(	SLPAREN	33	28
'gcd('	SSTRING	45	28
,	SCOMMA	41	28
x	SIDENTIFIER	43	28
,	SCOMMA	41	28
', '	SSTRING	45	28
,	SCOMMA	41	28
y	SIDENTIFIER	43	28
,	SCOMMA	41	28
')'	SSTRING	45	28
)	SRPAREN	34	28
;	SSEMICOLON	37	28
gcd	SIDENTIFIER	43	29
(	SLPAREN	33	29
x	SIDENTIFIER	43	29
,	SCOMMA	41	29
y	SIDENTIFIER	43	29
)	SRPAREN	34	29
;	SSEMICOLON	37	29
writeln	SWRITELN	23	30
(	SLPAREN	33	30
'  = '	SSTRING	45	30
,	SCOMMA	41	30
result	SIDENTIFIER	43	30
)	SRPAREN	34	30
;	SSEMICOLON	37	30
x	SIDENTIFIER	43	32
:=	SASSIGN	40	32
7854	SCONSTANT	44	32
;	SSEMICOLON	37	32
y	SIDENTIFIER	43	33
:=	SASSIGN	40	33
3108	SCONSTANT	44	33
;	SSEMICOLON	37	33
writeln	SWRITELN	23	34
(	SLPAREN	33	34
'gcd('	SSTRING	45	34
,	SCOMMA	41	34
x	SIDENTIFIER	43	34
,	SCOMMA	41	34
', '	SSTRING	45	34
,	SCOMMA	41	34
y	SIDENTIFIER	43	34
,	SCOMMA	41	34
')'	SSTRING	45	34
)	SRPAREN	34	34
;	SSEMICOLON	37	34
gcd	SIDENTIFIER	43	35
(	SLPAREN	33	35
x	SIDENTIFIER	43	35
,	SCOMMA	41	35
y	SIDENTIFIER	43	35
)	SRPAREN	34	35
;	SSEMICOLON	37	35
writeln	SWRITELN	23	36
(	SLPAREN	33	36
'  = '	SSTRING	45	36
,	SCOMMA	41	36
result	SIDENTIFIER	43	36
)	SRPAREN	34	36
;	SSEMICOLON	37	36
end	SEND	8	37
.	SDOT	42	37
