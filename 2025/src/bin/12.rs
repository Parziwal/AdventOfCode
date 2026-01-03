advent_of_code::solution!(12);

pub fn part_one(input: &str) -> Option<u64> {
    let input: Vec<&str> = input.split("\n\n").collect();
    let presents_unit: Vec<usize> = input
        .iter()
        .take(input.len() - 1)
        .map(|p| p.chars().filter(|c| *c == '#').count())
        .collect();

    let regions_fit = input
        .last()
        .unwrap()
        .lines()
        .map(|r| {
            let (package_area, package_quantity) = r.split_once(":").unwrap();
            let (width, length) = package_area.split_once("x").unwrap();
            let width = width.parse::<usize>().unwrap();
            let length = length.parse::<usize>().unwrap();

            let package_quantity: usize = package_quantity
                .split_whitespace()
                .map(|q| q.parse::<usize>().unwrap())
                .enumerate()
                .fold(0, |fields, (idx, q)| {
                    println!("{:?}", (presents_unit[idx] * q));
                    fields + (presents_unit[idx] * q)
                });

            package_quantity <= width * length
        })
        .filter(|r| *r)
        .count();

    println!("{:?}", regions_fit);
    Some(regions_fit as u64)
}

pub fn part_two(_input: &str) -> Option<u64> {
    None
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_part_one() {
        let result = part_one(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(2));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, None);
    }
}
