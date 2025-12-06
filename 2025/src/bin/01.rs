advent_of_code::solution!(1);

enum Direction {
    Left,
    Right,
}

struct DialRotation {
    direction: Direction,
    distance: i64,
}

impl DialRotation {
    fn from_string(string: &str) -> DialRotation {
        let (direction, distance) = string.split_at(1);
        let direction = match direction {
            "L" => Direction::Left,
            "R" => Direction::Right,
            _ => unreachable!(),
        };
        let distance: i64 = distance.parse().unwrap();

        DialRotation {
            direction,
            distance,
        }
    }
}

pub fn part_one(input: &str) -> Option<u64> {
    let pointed_zero_count = input
        .lines()
        .map(DialRotation::from_string)
        .scan(50_i64, |dial_pos, rotation| {
            *dial_pos = match rotation.direction {
                Direction::Left => (*dial_pos - rotation.distance).rem_euclid(100),
                Direction::Right => (*dial_pos + rotation.distance) % 100,
            };

            Some(*dial_pos)
        })
        .filter(|&pos| pos == 0)
        .count();

    return Some(pointed_zero_count as u64);
}

pub fn part_two(input: &str) -> Option<u64> {
    let passed_zero_count: u64 = input
        .lines()
        .map(DialRotation::from_string)
        .scan(50_i64, |dial_pos, rotation| {
            let prev_dial_pos = *dial_pos;
            *dial_pos = match rotation.direction {
                Direction::Left => (*dial_pos - rotation.distance).rem_euclid(100),
                Direction::Right => (*dial_pos + rotation.distance) % 100,
            };

            let passed_zero = match rotation.direction {
                Direction::Left => {
                    let passed_zero = ((100 - prev_dial_pos + rotation.distance) / 100) as u64;
                    if prev_dial_pos == 0 { passed_zero - 1 } else { passed_zero }
                }
                Direction::Right => {
                    ((prev_dial_pos + rotation.distance) / 100) as u64
                }
            };

            Some(passed_zero)
        })
        .sum();

    return Some(passed_zero_count);
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
        assert_eq!(result, Some(6));
    }
}
