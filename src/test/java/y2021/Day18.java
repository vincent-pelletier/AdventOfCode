import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class Day18 extends AdventOfCode {

	@Test
	public void day18() {
		printHeader();
		List<String> input = readFileAsStrings("day18.txt");
		List<SnailfishNumber> sfns = input.stream().map(SnailfishNumber::from).collect(Collectors.toList());

		SnailfishNumber sfn = sfns.get(0);
		for(int i = 1; i < sfns.size(); i++) {
			//print("  " + sfn);
			//print("+ " + sfns.get(i));
			sfn = new SnailfishNumber(sfn, sfns.get(i));
			sfn.reduce();
			//print("= " + sfn);
			//print("");
		}
		print(sfn);
		print("Magnitude: " + sfn.getMagnitude());

		br();

		int highestMagnitude = 0;
		for(int i = 0; i < sfns.size(); i++) {
			for(int j = i + 1; j < sfns.size(); j++) {
				SnailfishNumber sfn1 = SnailfishNumber.from(input.get(i));
				SnailfishNumber sfn2 = SnailfishNumber.from(input.get(j));

				sfn = new SnailfishNumber(sfn1, sfn2);
				sfn.reduce();
				int magnitude = sfn.getMagnitude();
				if(magnitude > highestMagnitude) {
					highestMagnitude = magnitude;
				}

				sfn1 = SnailfishNumber.from(input.get(i));
				sfn2 = SnailfishNumber.from(input.get(j));

				sfn = new SnailfishNumber(sfn2, sfn1);
				sfn.reduce();
				magnitude = sfn.getMagnitude();
				if(magnitude > highestMagnitude) {
					highestMagnitude = magnitude;
				}
			}
		}
		print(highestMagnitude);


	}

	private static class SnailfishNumber {
		SnailfishNumber sfnLeft;
		SnailfishNumber sfnRight;
		SnailfishNumber parent;
		int left;
		int right;
		int depth = 0;
		String uuid = UUID.randomUUID().toString();

		public SnailfishNumber(int left, int right) {
			this.left = left;
			this.right = right;
		}

		public SnailfishNumber(int left, SnailfishNumber sfnRight) {
			this.left = left;
			this.sfnRight = sfnRight;

			sfnRight.incrementDepth();
			sfnRight.parent = this;
		}

		public SnailfishNumber(SnailfishNumber sfnLeft, int right) {
			this.sfnLeft = sfnLeft;
			this.right = right;

			sfnLeft.incrementDepth();
			sfnLeft.parent = this;
		}

		public SnailfishNumber(SnailfishNumber sfnLeft, SnailfishNumber sfnRight) {
			this.sfnLeft = sfnLeft;
			this.sfnRight = sfnRight;

			sfnLeft.incrementDepth();
			sfnLeft.parent = this;
			sfnRight.incrementDepth();
			sfnRight.parent = this;
		}

		public void incrementDepth() {
			depth++;
			if(sfnLeft != null) {
				sfnLeft.incrementDepth();
			}
			if(sfnRight != null) {
				sfnRight.incrementDepth();
			}
		}

		public void reduce() {
			while(explode()) {
				//prints("after explode: " + this);
			}
			if(split()) {
				//prints("after split:   " + this);
				reduce();
			}
		}

		public boolean explode() {
			// identify sfn with depth 4 that will explode
			SnailfishNumber sfn = findDepth4();
			if(sfn != null) {
				// explode
				SnailfishNumber explodersParent = sfn.parent;
				if(explodersParent != null) {
					// find closest left number and add sfn.left
					if(!explodersParent.childsAreEqual() && sfn.equals(explodersParent.sfnRight)) {
						// exploding number is on the right, put left to parent.left
						if(explodersParent.sfnLeft != null) {
							explodersParent.sfnLeft.addToRight(sfn.left);
						} else {
							explodersParent.left += sfn.left;
						}
					} else if(explodersParent.sfnRight == null) {
						SnailfishNumber grandParentCopy = explodersParent.parent;
						SnailfishNumber parentCopy = explodersParent;
						while(grandParentCopy != null && !parentCopy.equals(grandParentCopy.sfnRight) && grandParentCopy.sfnLeft != null) {
							parentCopy = grandParentCopy;
							grandParentCopy = grandParentCopy.parent;
						}
						if(grandParentCopy != null) {
							if(grandParentCopy.sfnLeft != null) {
								grandParentCopy.sfnLeft.addToRight(sfn.left);
							} else {
								grandParentCopy.left += sfn.left;
							}
						}
					} else {
						// exploding number is on the left, put in the left of the parent
						SnailfishNumber grandParent = getGrandParentThatHasDifferentLeftChildThan(sfn);
						if(grandParent != null) {
							if(grandParent.sfnLeft != null) {
								grandParent.sfnLeft.addToRight(sfn.left);
							} else {
								grandParent.left += sfn.left;
							}
						}
					}

					// find closest number to the right and add sfn.right
					if(sfn.equals(explodersParent.sfnLeft)) {
						// exploding number is on the left, put right to parent.right
						if(explodersParent.sfnRight != null) {
							explodersParent.sfnRight.addToLeft(sfn.right);
						} else {
							explodersParent.right += sfn.right;
						}
					} else if(explodersParent.sfnLeft == null) {
						SnailfishNumber grandParentCopy = explodersParent.parent;
						SnailfishNumber parentCopy = explodersParent;
						while(grandParentCopy != null && !parentCopy.equals(grandParentCopy.sfnLeft) && grandParentCopy.sfnRight != null) {
							parentCopy = grandParentCopy;
							grandParentCopy = grandParentCopy.parent;
						}
						if(grandParentCopy != null) {
							if(grandParentCopy.sfnRight != null) {
								grandParentCopy.sfnRight.addToLeft(sfn.right);
							} else {
								grandParentCopy.right += sfn.right;
							}
						}
					} else {
						// exploding number is on the right, put in the right of the parent
						SnailfishNumber grandParent = getGrandParentThatHasDifferentRightChildThan(sfn);
						if(grandParent != null) {
							if(grandParent.sfnRight != null) {
								grandParent.sfnRight.addToLeft(sfn.right);
							} else {
								grandParent.right += sfn.right;
							}
						}
					}
				}

				// replace sfn pair that exploded with 0
				if(sfn.equals(explodersParent.sfnLeft)) {
					explodersParent.left = 0;
					explodersParent.sfnLeft = null;
				} else if(sfn.equals(explodersParent.sfnRight)) {
					explodersParent.right = 0;
					explodersParent.sfnRight = null;
				} else {
					prints("Exploding but not a parent's child?");
				}
				return true;
			}
			return false;
		}

		public boolean childsAreEqual() {
			if(sfnLeft == null) return false;
			if(sfnRight == null) return false;
			return sfnLeft.equals(sfnRight);
		}

		private SnailfishNumber getGrandParentThatHasDifferentLeftChildThan(SnailfishNumber sfn) {
			SnailfishNumber p = sfn.parent;
			while(p != null && sfn.equals(p.sfnLeft)) {
				sfn = p;
				p = p.parent;
			}
			return p;
		}

		private SnailfishNumber getGrandParentThatHasDifferentRightChildThan(SnailfishNumber sfn) {
			SnailfishNumber p = sfn.parent;
			while(p != null && sfn.equals(p.sfnRight)) {
				sfn = p;
				p = p.parent;
			}
			return p;
		}

		public void addToLeft(int v) {
			if(sfnLeft != null) {
				sfnLeft.addToLeft(v);
			} else {
				left += v;
			}
		}

		public void addToRight(int v) {
			if(sfnRight != null) {
				sfnRight.addToRight(v);
			} else {
				right += v;
			}
		}

		private SnailfishNumber findDepth4() {
			List<SnailfishNumber> match = new ArrayList<>();

			findFirstDepth4FromLeft(match);

			return !match.isEmpty() ? match.get(0) : null;
		}

		private void findFirstDepth4FromLeft(List<SnailfishNumber> match) {
			if(match == null || !match.isEmpty()) {
				return;
			}
			if(sfnLeft != null) {
				sfnLeft.findFirstDepth4FromLeft(match);
			}
			if(depth == 4) {
				match.add(this);
			}
			if(!match.isEmpty()) return;
			if(sfnRight != null) {
				sfnRight.findFirstDepth4FromLeft(match);
			}
		}

		public boolean split() {
			String copy = toString();
			List<String> numbers = Stream.of(toString().replace("[", ",").replace("]", ",").split(",")).filter(s -> !s.isEmpty()).collect(Collectors.toList());

			List<SnailfishNumber> match = new ArrayList<>();
			for(String number : numbers) {
				int idx = copy.indexOf(number);
				Integer num = Integer.valueOf(number);
				if(num > 9) {
					boolean isLeft = "[".equals(copy.charAt(idx-1) + "");
					findFirstMatchFromLeft(num, isLeft, match);
					break;
				}
				copy = copy.substring(idx+1, copy.length());
			}
			if(!match.isEmpty()) {
				SnailfishNumber sfn = match.get(0);
				if(sfn.left > 9) {
					sfn.sfnLeft = new SnailfishNumber((int)Math.floor((double)sfn.left/2), (int)Math.ceil((double)sfn.left/2));
					sfn.sfnLeft.depth = sfn.depth + 1;
					sfn.sfnLeft.parent = sfn;
					sfn.left = 0;
				} else {
					sfn.sfnRight = new SnailfishNumber((int)Math.floor((double)sfn.right/2), (int)Math.ceil((double)sfn.right/2));
					sfn.sfnRight.depth = sfn.depth + 1;
					sfn.sfnRight.parent = sfn;
					sfn.right = 0;
				}
				return true;
			}
			return false;
		}

		private void findFirstMatchFromLeft(int value, boolean isLeft, List<SnailfishNumber> match) {
			if(match == null || !match.isEmpty()) {
				return;
			}
			if(sfnLeft != null) {
				sfnLeft.findFirstMatchFromLeft(value, isLeft, match);
			}
			if((isLeft && value == left) || (!isLeft && value == right)) {
				match.add(this);
			}
			if(!match.isEmpty()) return;
			if(sfnRight != null) {
				sfnRight.findFirstMatchFromLeft(value, isLeft, match);
			}
		}

		public int getMagnitude() {
			return 3 * getLeft() + 2 * getRight();
		}

		private int getLeft() {
			return sfnLeft != null ? sfnLeft.getMagnitude() : left;
		}

		private int getRight() {
			return sfnRight != null ? sfnRight.getMagnitude() : right;
		}

		private String getLeftStr() {
			return sfnLeft != null ? sfnLeft.toString() : String.valueOf(left);
		}

		private String getRightStr() {
			return sfnRight != null ? sfnRight.toString() : String.valueOf(right);
		}

		@Override
		public String toString() {
			return "[" + getLeftStr() + "," + getRightStr() + "]";
		}

		@Override
		public boolean equals(Object o) {
			if(o == null) return false;
			SnailfishNumber other = (SnailfishNumber)o;
			return toString().equals(other.toString());
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		public static SnailfishNumber from(String str) {
			List<String> numbers = Stream.of(str.replace("[", ",").replace("]", ",").split(",")).filter(s -> !s.isEmpty()).collect(Collectors.toList());

			String copy = str;
			List<String> numbersWithSide = new ArrayList<>();
			for(String number : numbers) {
				int idx = copy.indexOf(number);
				if("[".equals(copy.charAt(idx-1) + "")) {
					number += "+L";
				} else if("]".equals(copy.charAt(idx+1) + "")) {
					number += "+R";
				}
				numbersWithSide.add(number);
				copy = copy.substring(idx+1, copy.length());
			}
			numbers.clear();
			numbers.addAll(numbersWithSide);

			Map<String, List<Integer>> usedIndices = new HashMap<>();
			// find inner-most pair on the left
			Map<String, SnailfishNumber> sfns = new HashMap<>();
			while(numbers.size() > 1) {
				for(int i = 0; i < numbers.size() - 1; i++) {
					String left = numbers.get(i);
					String right = numbers.get(i+1);
					if(left.contains("L") && right.contains("R")) {
						left = left.replace("+L", "");
						right = right.replace("+R", "");
					} else {
						continue;
					}
					String s = "[" + (left.length() == 1 ? left : sfns.get(left)) + "," + (right.length() == 1 ? right : sfns.get(right)) + "]";
					int start = 0;
					if(usedIndices.containsKey(s)) {
						start = usedIndices.get(s).get(usedIndices.get(s).size() - 1) + 5;
					}
					int index = str.substring(start, str.length()).indexOf(s) + start;
					if(index != -1) {
						if(!usedIndices.containsKey(s)) {
							usedIndices.put(s, new ArrayList<>());
						}
						usedIndices.get(s).add(index);
						SnailfishNumber sfn;
						if(left.length() == 1) {
							if(right.length() == 1) {
								sfn = new SnailfishNumber(Integer.valueOf(left), Integer.valueOf(right));
							} else {
								sfn = new SnailfishNumber(Integer.valueOf(left), sfns.get(right));
							}
						} else {
							if(right.length() == 1) {
								sfn = new SnailfishNumber(sfns.get(left), Integer.valueOf(right));
							} else {
								sfn = new SnailfishNumber(sfns.get(left), sfns.get(right));
							}
						}
						String side = "";
						if(index > 0) {
							String prev = str.charAt(index - 1) + "";
							side = prev.equals("[") ? "+L" : "+R";
						}
						sfns.put(sfn.uuid, sfn);
						// replace numbers with sfn
						numbers.remove(i);
						numbers.remove(i);
						numbers.add(i, sfn.uuid + side);
						break;
					}
				}
			}
			return sfns.get(numbers.get(0));
		}
	}
}
