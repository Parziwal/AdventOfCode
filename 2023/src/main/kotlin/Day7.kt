import java.io.File
import kotlin.math.min
import kotlin.math.pow

fun main() {
    println("Day 7")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay7()}")
    println("Part Two: ${partTwoOfDay7()}")
    println("-----------------------------------")
}

enum class CamelCardHandType(val value: Int) {
    FiveOfKind(1),
    FourOfKind(2),
    FullHouse(3),
    ThreeOfKind(4),
    TwoPair(5),
    OnePair(6),
    HighCard(7),
}

data class CamelCardHand(val cards: String, val bid: Int, val handType: CamelCardHandType)

fun String.getSameCardsCount(jIsJoker: Boolean = false): List<Int> {
    var cards = this.toMutableList()
    var jokerCount = 0
    if (jIsJoker) {
        cards = this.filter { it != 'J' }.toMutableList()
        jokerCount = 5 - cards.size
    }

    var sameCardsCount = mutableListOf<Int>()
    while (cards.isNotEmpty()) {
        var count = 0
        var i = 1
        while(i < cards.size) {
            if (cards.first() == cards[i]) {
                count++
                cards.removeAt(i)
            } else {
                i++
            }
        }
        cards.removeAt(0)
        if (count > 0) {
            sameCardsCount.add(count + 1)
        }
    }

    sameCardsCount = sameCardsCount.sorted().toMutableList()
    if (jIsJoker && sameCardsCount.isNotEmpty()) {
        sameCardsCount[sameCardsCount.size - 1] += jokerCount
    } else if (jIsJoker) {
        sameCardsCount.add(min(jokerCount + 1, 5))
    }
    return sameCardsCount
}

fun String.camelCardsToValue(jIsJoker: Boolean = false): Double {
    var value = 0.0
    var rank = 5
    for (card in this) {
        value += when(card) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if(jIsJoker) 1 else 11
            'T' -> 10
            else -> card.digitToInt()
        } * 100.0.pow(rank)
        rank--
    }

    return value
}

fun calculateTotalWinnings(jIsJoker: Boolean): Int {
    var rank = 1
    val totalWinnings = File("src/main/resources/day7.txt")
        .readLines()
        .map { line ->
            val cards = line.split(' ')[0]
            val bid = line.split(' ')[1].toInt()

            val sameCardCount = cards.getSameCardsCount(jIsJoker = jIsJoker)
            val handType = when {
                sameCardCount.size == 1 && sameCardCount.first() == 5 -> CamelCardHandType.FiveOfKind
                sameCardCount.size == 1 && sameCardCount.first() == 4 -> CamelCardHandType.FourOfKind
                sameCardCount.size == 2 && sameCardCount[0] == 2 && sameCardCount[1] == 3 -> CamelCardHandType.FullHouse
                sameCardCount.size == 1 && sameCardCount.first() == 3 -> CamelCardHandType.ThreeOfKind
                sameCardCount.size == 2 && sameCardCount[0] == 2 && sameCardCount[1] == 2 -> CamelCardHandType.TwoPair
                sameCardCount.size == 1 && sameCardCount.first() == 2 -> CamelCardHandType.OnePair
                else -> CamelCardHandType.HighCard
            }

            CamelCardHand(cards, bid, handType)
        }
        .sortedWith(compareByDescending<CamelCardHand> { it.handType.value }
            .thenBy { it.cards.camelCardsToValue(jIsJoker = jIsJoker) })
        .map { it.bid }
        .reduce { acc, bid ->
            acc + bid * ++rank
        }

    return totalWinnings
}

fun partOneOfDay7() = calculateTotalWinnings(jIsJoker = false)
fun partTwoOfDay7() = calculateTotalWinnings(jIsJoker = true)