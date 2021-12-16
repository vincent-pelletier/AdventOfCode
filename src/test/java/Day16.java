import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class Day16 extends AdventOfCode {

	@Test
	public void day16() {
		printHeader();
		for(String hex : readFileAsStrings("day16.txt")) {
			String bin = hex2bin(hex);
			print(hex);
			//print(bin);

			Bits bits = new Bits(bin);
			//bits.printTree();
			print("Total version " + bits.getTotalVersion());
			print("Total value " + bits.getValue());
		}
	}

	private class Bits {
		String version;
		String type;
		List<String> groups = new ArrayList<>(); // for literal values
		Bits next;

		String lengthTypeId;
		String subPacketsLength;
		Bits subPackets;
		int binaryLength;

		boolean iterated = false;

		public Bits(String bin) {
			this(bin, -1);
		}

		public Bits(String bin, int depthRemaining) {
			version = bin.substring(0, 3);
			type = bin.substring(3, 6);
			int idx = 6;
			if(getType() == 4) { // literal
				while(bin.length() >= idx + 5) {
					groups.add(bin.substring(idx, idx+5));
					if(bin.charAt(idx) == '0') {
						// that was the last one
						idx += 5;
						break;
					}
					idx += 5;
				}
			} else {
				// operator
				lengthTypeId = bin.substring(idx, ++idx);
				int subPacketsBitsLength = lengthTypeId.equals("0") ? 15 : 11;
				subPacketsLength = bin.substring(idx, idx + subPacketsBitsLength);
				idx += subPacketsBitsLength;
				if(lengthTypeId.equals("0")) {
					// send only getSubPacketLength() amount of bits
					subPackets = new Bits(bin.substring(idx, idx + getSubPacketLength()), -1);
				} else {
					// send all bits but with depth remaining = getSubPacketLength() - 1
					// depth remaining will stop looking for next Bits once 0
					subPackets = new Bits(bin.substring(idx, bin.length()), getSubPacketLength() - 1);
				}
				Bits lastSubPacket = null;
				if(lengthTypeId.equals("0")) {
					int breakAfter = idx + getSubPacketLength();
					while(idx < breakAfter) {
						if(lastSubPacket == null) {
							lastSubPacket = subPackets;
						} else {
							lastSubPacket = lastSubPacket.next;
						}
						idx += lastSubPacket.binaryLength;
					}
				} else {
					for(int i = 0; i < getSubPacketLength(); i++) {
						if(lastSubPacket == null) {
							lastSubPacket = subPackets;
						} else {
							lastSubPacket = lastSubPacket.next;
						}
						idx += lastSubPacket.binaryLength;
					}
				}
				if(lastSubPacket.next != null) {
					print("Expected to end, had " + getSubPacketLength() + " subpackets");
					throw new IllegalArgumentException();
				}
			}
			binaryLength = idx;
			String remaining = bin.substring(idx, bin.length());
			if(remaining.replace("0", "").isEmpty()) {
				// empty or only trailing 0s, the end
			} else {
				if(depthRemaining == -1) {
					next = new Bits(remaining);
				} else if(depthRemaining > 0) {
					next = new Bits(remaining, depthRemaining - 1);
				}
			}
		}

		public int getVersion() {
			return bin2dec(version);
		}

		public int getTotalVersion() {
			return getVersion() +
					(next != null ? next.getTotalVersion() : 0) +
					(subPackets != null ? subPackets.getTotalVersion() : 0);
		}

		public int getType() {
			return bin2dec(type);
		}

		public int getSubPacketLength() {
			return bin2dec(subPacketsLength);
		}

		public long getValue() {
			if(!groups.isEmpty()) {
				String bin = "";
				for(String group : groups) {
					bin += group.substring(1, 5);
				}
				return bin2dec2(bin);
			}
			List<Long> values = getSubValues();
			switch(getType()) {
				case 0:
					return values.stream().mapToLong(v -> v).sum();
				case 1:
					long product = 1L;
					for(Long value : values) {
						product *= value;
					}
					return product;
				case 2:
					return Collections.min(values);
				case 3:
					return Collections.max(values);
				case 5:
					if(values.size() != 2) throw new IllegalArgumentException();
					return values.get(0) > values.get(1) ? 1 : 0;
				case 6:
					if(values.size() != 2) throw new IllegalArgumentException();
					return values.get(0) < values.get(1) ? 1 : 0;
				case 7:
					if(values.size() != 2) throw new IllegalArgumentException();
					return values.get(0).equals(values.get(1)) ? 1 : 0;
			}
			throw new IllegalArgumentException();
		}

		private List<Long> getSubValues() {
			List<Long> values = new ArrayList<>();
			Bits child = subPackets;
			while(child != null) {
				values.add(child.getValue());
				child = child.next;
			}
			return values;
		}

		public void printTree() {
			print2("---TREE START---");
			printTree(0);
			print("");
			print("---TREE END---");
		}

		public void printTree(int depth) {
			print("");
			for(int i = 0; i < depth; i++) {
				print2(" ");
			}
			print2(this);
			Bits sub = subPackets;
			while(sub != null && !sub.iterated) {
				sub.iterated = true;
				sub.printTree(depth + 1);
				sub = sub.subPackets;
			}
			if(next != null) {
				next.printTree(depth);
			}
		}

		@Override
		public String toString() {
			return (type.equals("100") ? "Lit" : "Op.")
					+ " v" + getVersion()
					+ (type.equals("100") ? (" val " + getValue()) : (" t" + getType()));
		}
	}
}
