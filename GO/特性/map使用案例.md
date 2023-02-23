## map使用案例

```go
package main

import "fmt"

//test map
//1.create map
//2.query map
//3.delete map
//4.use struct

type PersonInfo struct {
	ID      string
	Name    string
	Address string
}

func FindPerson(fID string, sheet map[string]PersonInfo) {
	person, ok := sheet[fID]
	if ok {
		fmt.Println("Found Person:", person.Name, "ID:",
			person.ID, "Address:", person.Address)
	} else {
		fmt.Println("Did not find the Person, ID:", fID)
	}
}
func main() {
	var personDB map[string]PersonInfo
	personDB = make(map[string]PersonInfo)

	personDB["12345"] = PersonInfo{"12345", "Tom", "Room 2003"}
	personDB["1"] = PersonInfo{"1", "Jay", "Room 2005"}

	FindPerson("1234", personDB)
	FindPerson("12345", personDB)
	delete(personDB, "12345")
	FindPerson("12345", personDB)
}
```

