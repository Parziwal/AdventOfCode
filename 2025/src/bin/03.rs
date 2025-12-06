advent_of_code::solution!(3);

fn find_max_joltage(battery_bank: &str, battery_count: usize) -> u64 {
    let battery_bank_len = battery_bank.len();
    let mut max_joltage = String::new();
    let mut current_idx = 0;

    for i in 0..battery_count {
        let (battery_idx, max_battery) = battery_bank
            [current_idx..(battery_bank_len - battery_count + 1 + i)]
            .char_indices()
            .max_by(|&(i1, c1), &(i2, c2)| match c1.cmp(&c2) {
                std::cmp::Ordering::Equal => i2.cmp(&i1),
                other => other,
            })
            .unwrap();

        current_idx += battery_idx + 1;
        max_joltage.push(max_battery);
    }

    max_joltage.parse::<u64>().unwrap()
}

pub fn part_one(input: &str) -> Option<u64> {
    let total_joltage: u64 = input
        .lines()
        .map(|battery_bank| find_max_joltage(battery_bank, 2))
        .sum();

    Some(total_joltage)
}

pub fn part_two(input: &str) -> Option<u64> {
    let total_joltage: u64 = input
        .lines()
        .map(|battery_bank| find_max_joltage(battery_bank, 12))
        .sum();

    Some(total_joltage)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_part_one() {
        let result = part_one(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(357));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(3121910778619));
    }
}
