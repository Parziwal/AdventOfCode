use std::collections::HashSet;

advent_of_code::solution!(2);

struct ProductIdRange {
    start: u64,
    end: u64,
}

impl ProductIdRange {
    fn from_string(string: &str) -> ProductIdRange {
        let (first_id, last_id) = string.split_once('-').unwrap();

        let first_id: u64 = first_id.parse().unwrap();
        let last_id: u64 = last_id.parse().unwrap();

        ProductIdRange {
            start: first_id,
            end: last_id,
        }
    }
}

fn num_digits(n: u64) -> usize {
    if n == 0 {
        return 1;
    }
    n.ilog10() as usize + 1
}

fn repeat_number(number: u64, repeat_times: usize) -> u64 {
    let len = num_digits(number);
    let mult = 10_u64.pow(len as u32);
    let mut result = number;

    for _ in 1..repeat_times {
        result = result * mult + number;
    }

    result
}

fn sum_invalid_ids_for_times(id_range: &ProductIdRange, repeat_times: usize) -> HashSet<u64> {
    let mut invalid_ids = HashSet::new();

    let len = num_digits(id_range.start);
    let mut repeat_part_len = len.div_ceil(repeat_times);

    let mut repeat_part = if repeat_part_len * repeat_times != len {
        10_u64.pow((repeat_part_len - 1) as u32)
    } else {
        id_range.start / 10_u64.pow((len - repeat_part_len) as u32)
    };

    loop {
        let current_id = repeat_number(repeat_part, repeat_times);
        if current_id > id_range.end {
            break;
        }
        if current_id >= id_range.start {
            invalid_ids.insert(current_id);
        }

        if num_digits(repeat_part) != num_digits(repeat_part + 1) {
            repeat_part_len += 1;
            repeat_part = 10_u64.pow((repeat_part_len - 1) as u32)
        } else {
            repeat_part += 1;
        }
    }

    invalid_ids
}

pub fn part_one(input: &str) -> Option<u64> {
    let invalid_ids_sum = input
        .split(',')
        .map(ProductIdRange::from_string)
        .map(|ranges| sum_invalid_ids_for_times(&ranges, 2).iter().sum::<u64>())
        .sum();

    Some(invalid_ids_sum)
}
pub fn part_two(input: &str) -> Option<u64> {
    let invalid_ids_sum: u64 = input
        .split(',')
        .map(ProductIdRange::from_string)
        .map(|ranges| {
            (2..=num_digits(ranges.end))
                .flat_map(|time| sum_invalid_ids_for_times(&ranges, time))
                .collect::<HashSet<u64>>()
                .iter()
                .sum::<u64>()
        })
        .sum();

    Some(invalid_ids_sum)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_part_one() {
        let result = part_one(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(1227775554));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(4174379265));
    }
}
