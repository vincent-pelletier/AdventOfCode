package y2022;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import common.AdventOfCode;

public class Day13 extends AdventOfCode {

	@Test
	public void test() {
		List<String> packets = readFileAsStrings();
		List<Packet> allPackets = new ArrayList<>();

		boolean debug = false;

		int total = 0;
		int idx = 0;
		for(int i = 0; i < packets.size(); i += 3) {
			Packet p1 = createPacket(packets.get(i));
			Packet p2 = createPacket(packets.get(i + 1));
			allPackets.add(p1);
			allPackets.add(p2);
			idx++;
			if(rightOrder(p1, p2, debug)) {
				total += idx;
			}
		}
		print(total);

		br();

		allPackets.add(createPacket("[[2]]"));
		allPackets.add(createPacket("[[6]]"));
		Collections.sort(allPackets, (a,b) -> a.isSmallerThan(b).comparator());

		int decoder = 1;
		for(int i = 0; i < allPackets.size(); i++) {
			String p = allPackets.get(i).toString();
			if("[[2]]".equals(p) || "[[6]]".equals(p)) {
				decoder *= (i+1);
			}
		}
		print(decoder);
	}

	private boolean rightOrder(Packet p1, Packet p2, boolean debug) {
		Comparison comp = p1.isSmallerThan(p2);
		if(debug) {
			print(p1);
			print(p2);
			print(comp);
			br();
		}
		return Comparison.SMALLER == comp;
	}

	Packet createPacket(String p) {
		String format = p.replace("[]", "-1").replace("[", ",").replace("]", ",");
		while(format.indexOf(",,") != -1) {
			format = format.replace(",,", ",");
		}

		List<Integer> values = Stream.of(format.split(",")).filter(s -> !s.isEmpty()).map(Integer::valueOf).collect(Collectors.toList());
		int numIdx = 0;

		Packet initial = null;
		List<Packet> openPackets = new ArrayList<>();
		for(int i = 0; i < p.length(); i++) {
			String s = charAt(p, i);
			switch(s) {
			case "[":
				Packet packet = new Packet();
				openPackets.add(packet);
				if(initial == null) {
					initial = packet;
				}
				break;
			case "]":
				Packet closed = openPackets.remove(openPackets.size() - 1);
				if("[".equals(charAt(p, i - 1))) {
					closed.list.add(new Packet(Packet.VALUE_EMPTY));
					assertEquals(Packet.VALUE_EMPTY, values.get(numIdx).intValue());
				}
				if(closed != initial) {
					openPackets.get(openPackets.size() - 1).list.add(closed);
				}
				break;
			case ",":
				numIdx++;
				break;
			default:
				int value = values.get(numIdx);
				if(value >= 10) {
					i++; // move 2 digits
				}
				openPackets.get(openPackets.size() - 1).list.add(new Packet(value));
				break;
			}
		}
		assertEquals(p, initial.toString());
		return initial;
	}

	/**
	 * Can be list or single value
	 *
	 */
	class Packet {
		static final int VALUE_LIST = -2;
		static final int VALUE_EMPTY = -1;

		List<Packet> list = new ArrayList<>();
		final int value;

		Packet() {
			value = VALUE_LIST;
		}

		Packet(int i) {
			value = i;
		}

		@Override
		public String toString() {
			if(value > VALUE_LIST) {
				if(value == VALUE_EMPTY) {
					return "";
				}
				return String.valueOf(value);
			}

			String s = "[";
			s += list.stream().map(Packet::toString).collect(Collectors.joining(","));
			s += "]";
			return s;
		}

		Comparison isSmallerThan(Packet o) {
			if(value > VALUE_LIST && o.value > VALUE_LIST) {
				if(value < o.value) return Comparison.SMALLER;
				if(value > o.value) return Comparison.HIGHER;
				return Comparison.CONTINUE;
			}

			if(value > VALUE_LIST && o.value == VALUE_LIST) {
				if(value == VALUE_EMPTY) return Comparison.SMALLER; // left ran out
				Packet p = new Packet();
				p.list.add(new Packet(value));
				return p.isSmallerThan(o);
			}

			if(value == VALUE_LIST && o.value > VALUE_LIST) {
				if(o.value == VALUE_EMPTY) return Comparison.HIGHER; // right ran out
				Packet po = new Packet();
				po.list.add(new Packet(o.value));
				return this.isSmallerThan(po);
			}

			// both are lists
			for(int i = 0; i < list.size(); i++) {
				if(i >= o.list.size()) { // right ran out first
					return Comparison.HIGHER;
				}
				Comparison comp = list.get(i).isSmallerThan(o.list.get(i));
				if(Comparison.CONTINUE == comp) continue;
				return comp;
			}
			if(o.list.size() > list.size()) { // left ran out first
				return Comparison.SMALLER;
			}
			return Comparison.CONTINUE;
		}
	}

	enum Comparison {
		SMALLER,
		CONTINUE,
		HIGHER;

		int comparator() {
			if(this == SMALLER) {
				return -1;
			} else if(this == HIGHER) {
				return 1;
			}
			return 0;
		}
	}
}
