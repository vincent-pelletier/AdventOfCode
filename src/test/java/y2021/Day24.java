import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class Day24 extends AdventOfCode {

	@Test
	public void day24() {
		printHeader();
		List<String> input = readFileAsStrings("day24.txt");

		/*
		inp a - Read an input value and write it to variable a.
		add a b - Add the value of a to the value of b, then store the result in variable a.
		mul a b - Multiply the value of a by the value of b, then store the result in variable a.
		div a b - Divide the value of a by the value of b, truncate the result to an integer, then store the result in variable a. (Here, "truncate" means to round the value toward zero.)
		mod a b - Divide the value of a by the value of b, then store the remainder in variable a. (This is also called the modulo operation.)
		eql a b - If the value of a and b are equal, then store the value 1 in variable a. Otherwise, store the value 0 in variable a.
		*/
		String highestSerial = null;

		List<String> prefixes = Arrays.asList(
				"135792468"
				);
		List<String> invalidPrefixes = Arrays.asList(
				"999999999",
				"888888888",
				"777777777",
				"666666666",
				"555555555",
				"444444444",
				"333333333",
				"222222222",
				"111111111",
				"246824682",
				"246813579",
				//"135792468",
				"135791357",
				"123456789",
				"987654321",
				"149253649",
				"987654321",
				"182764125",
				"112358132",
				"123561321",
				"248163264",
				"141592653",
				"314159265",
				"124816326",
				"139278124",
				"152512562",
				"525125625",
				"392781243",
				"111213141"
				);
		List<String> dups = new ArrayList<>();
		for(String prefix : prefixes) {
			if(invalidPrefixes.contains(prefix)) {
				dups.add(prefix);
				continue;
			}
			for(int a1 = 9; a1 > 0 && highestSerial == null; a1--) {
				for(int a2 = 9; a2 > 0 && highestSerial == null; a2--) {
					for(int a3 = 9; a3 > 0 && highestSerial == null; a3--) {
						for(int a4 = 9; a4 > 0 && highestSerial == null; a4--) {
							for(int a5 = 9; a5 > 0 && highestSerial == null; a5--) {
								String serial = prefix + a1 + a2 + a3 + a4 + a5;
								if(!serial.equals("13579246899979")) {
									continue;
								}
								print(serial);
								int idx = 0;
								Map<String, Long> vars = new HashMap<>();
								vars.put("w", 0L);
								vars.put("x", 0L);
								vars.put("y", 0L);
								vars.put("z", 0L);

								for(String row : input) {
									String op = row.split(" ")[0];
									String first = row.split(" ")[1];
									if(op.equals("inp")) {
										vars.put(row.replace("inp ", ""), Long.valueOf(serial.charAt(idx++) + ""));
										print("ops " + vars.get("z"));
										if(vars.get("z").equals(2368236822L)) {
											print("dbg");
										}
									} else {
										String second = row.split(" ")[2];
										Long secondValue = vars.containsKey(second) ? vars.get(second) : Long.valueOf(second);
										if(op.equals("add")) {
											vars.put(first, vars.get(first) + secondValue);
										} else if(op.equals("mul")) {
											vars.put(first, vars.get(first) * secondValue);
										} else if(op.equals("div")) {
											if(secondValue == 0) {
												break;
											}
											vars.put(first, Math.floorDiv(vars.get(first), secondValue));
										} else if(op.equals("mod")) {
											if(vars.get(first) < 0) {
												break;
											}
											if(secondValue <= 0) {
												break;
											}
											vars.put(first, Math.floorMod(vars.get(first), secondValue));
										} else if(op.equals("eql")) {
											vars.put(first, vars.get(first).equals(secondValue) ? 1L : 0L);
										} else {
											print("!");
										}
									}
								}
								print("ops " + vars.get("z"));
								if(vars.get("z") == 0) {
									highestSerial = serial;
								}
								br();
								if(z(serial) != vars.get("z")) {
									highestSerial = "exit";
									print("ops z " + vars.get("z"));
									//print(z(serial));
								}
								highestSerial = "exit";
							}
						}
					}
				}
			}
		}
		print("Highest: " + highestSerial);
		print("Dups: " + dups);

		/*for(int a1 = 9; a1 > 0 && highestSerial == null; a1--) {
			for(int a2 = 9; a2 > 0 && highestSerial == null; a2--) {
				for(int a3 = 9; a3 > 0 && highestSerial == null; a3--) {
					for(int a4 = 9; a4 > 0 && highestSerial == null; a4--) {
						for(int a5 = 9; a5 > 0 && highestSerial == null; a5--) {
							for(int a6 = 9; a6 > 0 && highestSerial == null; a6--) {
								for(int a7 = 9; a7 > 0 && highestSerial == null; a7--) {
									for(int a8 = 9; a8 > 0 && highestSerial == null; a8--) {
										for(int a9 = 9; a9 > 0 && highestSerial == null; a9--) {
											for(int a10 = 9; a10 > 0 && highestSerial == null; a10--) {
												for(int a11 = 9; a11 > 0 && highestSerial == null; a11--) {
													for(int a12 = 9; a12 > 0 && highestSerial == null; a12--) {
														for(int a13= 9; a13 > 0 && highestSerial == null; a13--) {
															for(int a14 = 9; a14 > 0 && highestSerial == null; a14--) {

																String serial = "" + a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 + a11 + a12 + a13 + a14;

															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}*/

		/*br();

		int idx = 0;
		Map<Integer, List<String>> eqs = new HashMap<>();
		for(String row : input) {
			int key = idx++ % 18;
			if(!eqs.containsKey(key)) {
				eqs.put(key, new ArrayList<>());
			}
			eqs.get(key).add(row);
		}
		for(List<String> values : eqs.values()) {
			print(new HashSet<>(values).size() == 1 ? new HashSet<>(values) : values);
		}*/
	}

	private long z(String serial) {
		/*
		for(int i = 0; i < 14; i++) {
			w = input				// new each time, const
			x = z % 26				// from z
			z = z / value (1 or 26)	// from z
			x = x + value (random)
			x = x == w ? 0 : 1
			y = 25 * x (1 or 0)		// new each time
			y = y + 1			// 1 or 26
			z = z * y
			y = w + value (random)
			y = y * x (1 or 0)
			z = z + y			// must be 0
		}
		*/
		List<Integer> divBy26 = Arrays.asList(3, 5, 6, 8, 11, 12, 13);
		int[] addX = new int[] {11, 11, 15, -14, 10, 0, -6, 13, -3, 13, 15, -2, -9, -2};
		int[] addY = new int[] {6, 14, 13, 1, 6, 13, 6, 3, 8, 14, 4, 7, 15, 1};
		long z = 0;
		List<Integer> in = new ArrayList<>();
		for(int i = 0; i < 14; i++) {
			int w = Integer.valueOf(serial.charAt(i) + "");
			if(w == 0) continue;
			if(i > 0) {
				print(i + ". [" + Math.floorMod(z, 26) + "] + " + addX[i] + " == " + w);
			}
			if(Math.floorMod(z, 26) + addX[i] == w) {
				print("In!!!");
				in.add(i);
				if(divBy26.contains(i)) {
					print("Dividing... " + z + "/" + 26 + " = " + Math.floorDiv(z, 26));
					z = Math.floorDiv(z, 26);
				}
			} else {
				if(divBy26.contains(i)) {
					print("Dividing... " + z + "/" + 26 + " = " + Math.floorDiv(z, 26));
					z = Math.floorDiv(z, 26);
				}
				z = z * 26 + w + addY[i]; // w et addY toujours positif
				print("+" + w + " +" + addY[i] + " = [" + (w + addY[i]) + "]");
			}
			print("z = " + z);
			print("in " + in);
		}
		// pour avoir 0 à la fin, je dois
		// rentrer dans le if pour rester à 0, qui est impossile pcq jamais addX[i] = w (1-9)
		// donc je dois resetter à 0 mon z, en ayant une valeur < 25 à 3,5,6,8,11,12,13.
		// iteration 13 (14e) je dois absolument rentrer dans le if pour rester à 0.
		// 13. 24 -2 = 22 != 9
		// !
		// w + addY[i] == next z % 26
		// donc prev + addY == this
		// addY[13] on s'en fout, on veut pas rentrer là
		// donc on veut rentrer a la dernière loop, donc addY[12] = 15
		// mais prevW + 15 = w, ça se peut pas.
		// si on rentrait avec addY[11]? C'est 7!
		// donc faut que [10] + 7 = [11]
		// je dois arranger mes digits pour toujours diviser et pas trop multiplier
		// chaque iteration je * 26 et 1/2 je /26. Je dois soit *26 et /26, ou equivaloir avec /26
		// (3, 5, 6, 8, 11, 12, 13)
		// int[] addX = new int[] {11, 11, 15, -14, 10, 0, -6, 13, -3, 13, 15, -2, -9, -2};
		// int[] addY = new int[] {6, 14, 13, 1, 6, 13, 6, 3, 8, 14, 4, 7, 15, 1};
		// ITERATION 13: rentrer dans le if pour pas finir par un +
		//
		// i = 0
		// IF 0 + 11 == w0 [never]
		//   z0 = w0 + 6;
		//
		// i = 1
		// IF w0 + 6 + 11  == w1 [never]
		//   z1 = 26(z0) + w1 + 14
		//
		// i = 2
		// IF w1 + 14 + 15 == w2 [never]
		//   z2 = 26(z1) + w2 + 13
		//
		// i = 3*
		// IF w2 + 13 - 14 == w3 --> MUST: [w2 - 1 = w3]
		// 	 z3 = z2 / 26
		//
		// i = 4
		// IF w3 + 1 - 10 == w4 [never]
		//   z4 = 26(z3) + w4 + 6
		//
		// i = 5*
		// IF w4 + 6 + 0 == w5 --> MUST: [w4 + 6 == w5]
		//   z5 = z4 / 26
		//
		// i = 6*
		// IF w5 + 13 - 6 == w6 --> MUST: [w5 + 7 == w6] (cannot enter 6 and 5...) TODO we never add w5 and 7.. find other value
		//   z6 = z5 / 26 OR z6 = 26(z5) + w6 + 6
		//
		// i = 7
		// IF w6 + 6 + 13 == w7 [never]
		//   z7 = 26(z6) + w7 + 3
		//
		// i = 8*
		// IF w7 + 3 - 3 == w8 --> MUST: [w7 == w8]
		//   z8 = z7 / 26
		//
		// i = 9
		// IF w8 + 8 + 13 == w9 [never]
		//   z9 = 26(z8) + w9 + 14
		//
		// i = 10
		// IF w9 + 14 + 15 == w10 [never]
		//   z10 = 26(z9) + w10 + 4
		//
		// i = 11*
		// IF w10 + 4 - 2 == w11 --> MUST: [w10 + 2 == w11]
		//   z11 = z10 / 26
		//
		// i = 12*
		// IF w11 + 7 - 9 == w12 --> MUST: [w11 - 2 == w12] // debug
		//   z12 = z11 / 26
		//
		// i = 13*
		// IF w12 + 15 - 2 == w13 [never]
		//   z13 = 26(z12) + w13 + 1
		//                   ^^^^^^^ WELP :(


		// 1: x26 (1)
		// 2: x26 (2)
		// 3: /26 (1)		*
		// 4: x26 (2)
		// 5: /26 (1)		*
		// 6: /26, x26 (1)	* ...rm x
		// 7: x26 (2)
		// 8: /26 (1)		*
		// 9: x26 (2)
		//10: x26 (3)
		//11: /26 (2)		*
		//12: /26, x26 (2)	* ...rm x
		//13: /26, x26 (2)	* ...rm x
		// I need to remove all 3 (or 2?) x26 to go to 0. Including the last one, because it adds...

		// (3, 5, 6, 8, 11, 12, 13)
		// int[] addX = new int[] {11, 11, 15, -14, 10, 0, -6, 13, -3, 13, 15, -2, -9, -2};
		// int[] addY = new int[] {6, 14, 13, 1, 6, 13, 6, 3, 8, 14, 4, 7, 15, 1};

		//print("final " + z);
		print("of " + divBy26);
		return z;
	}

	@Test
	public void testZ() {
		// 01234567890123
		//z("99982899917969");
		//   ^^^^x^^ ^^
		//   -1+6 == +2
		//          ^lower for i=12

		//in [3, 5, 8, 11, 12]
		//of [3, 5, 6, 8, 11, 12, 13]

		// z("99828299931381");
		//in [6, 8, 11, 12, 13]
		//of [3, 5, 6, 8, 11, 12, 13]

		//z("19983959947999");
		z("51983999947999");
		z2(5,1,9,8,3,9,9,9,9,4,7,9,9,9);
	}

	@Test
	public void bruteForce() { // runs in 42 mins... could change a1 to start at 5
		// rules (from w0)
		//	[w2 - 1 = w3]	--> a4 = a3 - 1
		//	[w4 + 6 == w5]	--> a6 = a5 + 6
		//	[w5 + 7 == w6] brute force from previous
		//	[w7 == w8]		--> a9 == a8
		//	[w10 + 2 == w11]--> a12 = a11 + 2
		//	[w11 - 2 == w12] brute force from previous
		//	........ == w13 brute force from previous

		long lowest = 999999999;
		String lowSerial = "";
		for(int a1 = 9; a1 > 0; a1--) {
			lowest = 999999999;
			for(int a2 = 9; a2 > 0; a2--) {
				for(int a3 = 9; a3 > 1; a3--) {
					int a4 = a3 - 1;
					//for(int a4 = 3; a4 > 0; a4--) {
						for(int a5 = /*3*/9; a5 > 0; a5--) {
							//int a6 = a5 + 6;				// this condition was removed to try all the possibilities
							for(int a6 = 9; a6 > 0; a6--) { // and make it work for the next iteration
								for(int a7 = 9; a7 > 0; a7--) {  // depends on previous
									//print("" + a1 + a2 + a3 + a4 + a5 + a6 + a7 + "..." + new java.util.Date());
									for(int a8 = 9; a8 > 0; a8--) {
										int a9 = a8;
										//for(int a9 = 9; a9 > 0; a9--) {
											for(int a10 = 9; a10 > 0; a10--) {
												for(int a11 = 7; a11 > 0; a11--) {
													int a12 = a11 + 2;
													//for(int a12 = 9; a12 > 0; a12--) {
														for(int a13 = 9; a13 > 0; a13--) {
															for(int a14 = 9; a14 > 0; a14--) {
																long z = z2(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14);
																if(lowest > z) {
																	lowest = z;
																	lowSerial = "" + a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 + a11 + a12 + a13 + a14;
																}
																if(z == 0L) {
																	print("Highest " + a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 + a11 + a12 + a13 + a14);
																	return;
																}
															}
														}
													//}
												}
											}
										//}
									}
								}
							}//
						}
					//}
				}
			}
			print("Lowest of " + a1 + "... " + lowest + " (" + lowSerial + ")");

			// Output: Test ran in 42 mins... there is a pattern.
			//
			// Could change a1 to start at 5, or debug the pattern
			// and make it super fast. But I've spend too long on
			// this already... committing as is. :)
			//
			// Lowest of 9... 2 (91983999947991)
			// Lowest of 8... 2 (81983999947991)
			// Lowest of 7... 2 (71983999947991)
			// Lowest of 6... 2 (61983999947991)
			// Got 5,6,13!!
			// 51983999947999
			// Highest 51983999947999
		}
	}

	@Test
	public void bruteForcePart2() {
		// rules (from w0)
		//	[w2 - 1 = w3]	--> a4 = a3 - 1
		//	[w4 + 6 == w5]	--> a6 = a5 + 6
		//	[w5 + 7 == w6] brute force from previous
		//	[w7 == w8]		--> a9 == a8
		//	[w10 + 2 == w11]--> a12 = a11 + 2
		//	[w11 - 2 == w12] brute force from previous
		//	........ == w13 brute force from previous

		long lowest = 999999999;
		String lowSerial = "";
		for(int a1 = 1; a1 < 10; a1++) {
			lowest = 999999999;
			for(int a2 = 1; a2 < 10; a2++) {
				for(int a3 = 2; a3 < 10; a3++) {
					int a4 = a3 - 1;
					//for(int a4 = 3; a4 > 0; a4--) {
						for(int a5 = 1; a5 < 10; a5++) {
							//int a6 = a5 + 6;				// this condition was removed to try all the possibilities
							for(int a6 = 1; a6 < 10; a6++) { // and make it work for the next iteration
								for(int a7 = 1; a7 < 10; a7++) {  // depends on previous
									//print("" + a1 + a2 + a3 + a4 + a5 + a6 + a7 + "..." + new java.util.Date());
									for(int a8 = 1; a8 < 10; a8++) {
										int a9 = a8;
										//for(int a9 = 9; a9 > 0; a9--) {
											for(int a10 = 1; a10 < 10; a10++) {
												for(int a11 = 1; a11 < 8; a11++) {
													int a12 = a11 + 2;
													//for(int a12 = 9; a12 > 0; a12--) {
														for(int a13 = 1; a13 < 10; a13++) {
															for(int a14 = 1; a14 < 10; a14++) {
																long z = z2(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14);
																if(lowest > z) {
																	lowest = z;
																	lowSerial = "" + a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 + a11 + a12 + a13 + a14;
																}
																if(z == 0L) {
																	print("Highest " + a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 + a11 + a12 + a13 + a14);
																	return;
																}
															}
														}
													//}
												}
											}
										//}
									}
								}
							}//
						}
					//}
				}
			}
			print("Lowest of " + a1 + "... " + lowest + " (" + lowSerial + ")");
		}
	}

	List<Integer> divBy26 = Arrays.asList(3, 5, 6, 8, 11, 12, 13);
	int[] addX = new int[] {11, 11, 15, -14, 10, 0, -6, 13, -3, 13, 15, -2, -9, -2};
	int[] addY = new int[] {6, 14, 13, 1, 6, 13, 6, 3, 8, 14, 4, 7, 15, 1};

	private long z2(int... nums) {
		long z = 0;
		int got5and6and13 = 0;
		for(int i = 0; i < 14; i++) {
			int w = nums[i];
			if(Math.floorMod(z, 26) + addX[i] == w) {
				if(i == 5 || i == 6 || i == 13) {
					got5and6and13++;
				}
				if(divBy26.contains(i)) {
					z = Math.floorDiv(z, 26);
				}
			} else {
				if(divBy26.contains(i)) {
					z = Math.floorDiv(z, 26);
				}
				z = z * 26 + w + addY[i];
			}
		}
		if(got5and6and13 == 3) {
			print("Got 5,6,13!!");
			print("" + nums[0] + nums[1] + nums[2] + nums[3] + nums[4] + nums[5] + nums[6] + nums[7] + nums[8] + nums[9] + nums[10] + nums[11] + nums[12] + nums[13]);
		}
		return z;
	}
}
