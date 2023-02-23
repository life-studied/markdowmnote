package main

import (
	"bufio"
	"flag"
	"fmt"
	"go_study/internal/algorithms/bubblesort"
	"go_study/internal/algorithms/qsort"
	"io"
	"os"
	"strconv"
	"time"
)

var infile *string = flag.String("i", "infile", "File contains values for sorting")
var outfile *string = flag.String("o", "outfile", "File to receive sorted values")
var algorithm *string = flag.String("a", "qsort", "Sort algorithms")

func readValues(infile string) (values []int, err error) {
	file, err := os.Open(infile)
	if err != nil {
		fmt.Println("Failed to open the input file ", infile)
		return
	}

	defer file.Close()

	br := bufio.NewReader(file)

	values = make([]int, 0)

	for {
		line, isPrefix, err1 := br.ReadLine()
		if err1 != nil {
			if err1 != io.EOF {
				err = err1
			}
			break
		}

		if isPrefix {
			fmt.Println("A too long line, seems unexpected")
			return
		}

		str := string(line)
		value, err1 := strconv.Atoi(str)

		if err1 != nil {
			err = err1
			return
		}
		values = append(values, value)
	}
	return
}

func writeValues(values []int, outfile string) error {
	file, err := os.Create(outfile)
	if err != nil {
		fmt.Println("Failed to create the output file ", outfile)
		return err
	}

	defer file.Close()

	for _, value := range values {
		str := strconv.Itoa(value)
		file.WriteString(str + "\n")
	}
	return nil
}

func main() {
	flag.Parse()

	if infile != nil {
		fmt.Println("infile = ", *infile, "outfile = ", *outfile, "algorithm = ", *algorithm)

	}

	values, err := readValues(*infile)
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println("Read Successed!")
		t1 := time.Now()
		switch *algorithm {
		case "qsort":
			qsort.QuickSort(values)
		case "bubblesort":
			bubblesort.BubbleSort(values)
		default:
			fmt.Println("Sorting algorighm", *algorithm,
				"is either unknown or unsupported.")
		}
		t2 := time.Now()
		fmt.Println("The Sorting process costs", t2.Sub(t1),
			"to complete.")

		err = writeValues(values, *outfile)
		if err != nil {
			fmt.Println(err)
		} else {
			fmt.Println("Write Successed!")
		}
	}

}
