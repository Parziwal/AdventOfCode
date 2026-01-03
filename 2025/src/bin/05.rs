use std::ops::RangeInclusive;

advent_of_code::solution!(5);

fn parse_range(line: &str) -> RangeInclusive<u64> {
    let (start_id, end_id) = line.split_once('-').unwrap();
    let start_id = start_id.parse::<u64>().unwrap();
    let end_id = end_id.parse::<u64>().unwrap();

    start_id..=end_id
}

pub fn part_one(input: &str) -> Option<u64> {
    let mut lines = input.lines();
    let available_ingrediant_id_ranges: Vec<RangeInclusive<u64>> = lines
        .by_ref()
        .take_while(|line| !line.is_empty())
        .map(parse_range)
        .collect();

    let available_ingrediant_count = lines
        .filter(|line| {
            let ingredient_id = line.parse::<u64>().unwrap();

            available_ingrediant_id_ranges
                .iter()
                .any(|id_range| id_range.contains(&ingredient_id))
        })
        .count();

    Some(available_ingrediant_count as u64)
}

pub fn part_two(input: &str) -> Option<u64> {
    let mut available_ingrediant_id_ranges: Vec<RangeInclusive<u64>> = input
        .lines()
        .take_while(|line| !line.is_empty())
        .map(parse_range)
        .collect();

    available_ingrediant_id_ranges.sort_by(|r1, r2| match r1.start().cmp(r2.start()) {
        std::cmp::Ordering::Equal => r1.end().cmp(r2.end()),
        other => other,
    });

    let mut unique_ranges = vec![available_ingrediant_id_ranges[0].clone()];
    for range in &available_ingrediant_id_ranges[1..] {
        let last_range = unique_ranges.last_mut().unwrap();

        if range.start() <= last_range.end() && range.end() > last_range.end() {
            *last_range = (*last_range.start())..=*range.end();
        } else if range.start() > last_range.end() {
            unique_ranges.push(range.clone());
        }
    }

    let available_ingrediant_count = unique_ranges
        .iter()
        .map(|range| range.end() - range.start() + 1)
        .sum();

    Some(available_ingrediant_count)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_part_one() {
        let result = part_one(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(3));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(14));
    }
}
