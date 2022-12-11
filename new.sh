#!/bin/bash

if (($# == 1)); then
	YEAR=$(date +%Y)
	DAY=$1
	if (($DAY < 10)); then
		DAY="0"$DAY
	fi
elif (($# == 2)); then
	YEAR=$1
	DAY=$2
else
	echo "Must specify year and day, or only day for current year:"
	echo "	./new.sh 1"
	echo "	./new.sh 2022 1"
	exit
fi

touch src/test/resources/$YEAR/day$DAY.txt
dos2unix src/test/resources/$YEAR/day$DAY.txt

echo """package y$YEAR;

import org.junit.Test;

import common.AdventOfCode;

public class Day$DAY extends AdventOfCode {

	@Test
	public void test() {

	}
}""" > src/test/java/y$YEAR/Day$DAY.java

echo "Created template files for $YEAR-12-$DAY"