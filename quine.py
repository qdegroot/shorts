# Really this is more of a quine *builder*
# So I felt free to put some comments in
# The point is that it increments id_num in the file it copies itself to.
# Also it increments the new filename because I wanted that to happen



# The basics
q = chr(34)
nl = chr(10)
c = chr(44)
id_num = 0

# Strings used
strings = [
"q = chr(34)",
"nl = chr(10)",
"c = chr(44)",
'id_num = ' + str(id_num + 1),
"'id_num = ' + str(id_num + 1)",
"strings = [",
"]",
"f = open('quine' + str(id_num + 1) + '.py', 'w')",
"for num in range(0, 4):",
"    f.write(strings[num] + nl)",
"f.write(strings[5] + nl)",
"for num in range(0, 3):",
"    f.write(q + strings[num] + q + c + nl)",
"f.write(strings[4] + c + nl)",
"for num in range(4, len(strings) - 1):",
"    f.write(q + strings[num] + q + c + nl)",
"f.write(q + strings[len(strings) - 1] + q + nl)",
"f.write(strings[6] + nl)",
"for num in range(7, len(strings)):",
"    f.write(strings[num] + nl)",
"f.close()",
"print('Made quine' + str(id_num + 1) + '.py!')"
]

# So I figure let's do a cool file thing
f = open('quine' + str(id_num + 1) + '.py', 'w')

# This segment writes the whole bit up to the start of the strings list
# The id_num variable gets written differently every time
for num in range(0, 4):
    f.write(strings[num] + nl)

# This segment writes the strings list and everything inside
# There was some inconsistency in element 4 because I thought it was necessary?
f.write(strings[5] + nl)
for num in range(0, 3):
    f.write(q + strings[num] + q + c + nl)
f.write(strings[4] + c + nl)
for num in range(4, len(strings) - 1):
    f.write(q + strings[num] + q + c + nl)
f.write(q + strings[len(strings) - 1] + q + nl)
f.write(strings[6] + nl)

# This writes out everything after the strings list
# All the quoting made me go blind, weep for me
for num in range(7, len(strings)):
    f.write(strings[num] + nl)

# Cleanliness is next to quineliness
f.close()

# Arbitrary (sort of) system code can go down here
# Y'know, if it's wanted
print('Made quine' + str(id_num + 1) + '.py!')