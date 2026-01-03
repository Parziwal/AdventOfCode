advent_of_code::solution!(9);

struct Point2D {
    x: i64,
    y: i64,
}

impl Point2D {
    fn new(x: i64, y: i64) -> Point2D {
        Point2D { x, y }
    }
}

pub fn part_one(input: &str) -> Option<u64> {
    let points: Vec<Point2D> = input
        .lines()
        .map(|coordinates| {
            let (x, y) = coordinates.split_once(',').unwrap();
            let x = x.parse::<i64>().unwrap();
            let y = y.parse::<i64>().unwrap();

            Point2D::new(x, y)
        })
        .collect();

    let mut max_area = 0;
    for i in 0..(points.len() - 1) {
        for j in i..points.len() {
            let area =
                (points[i].x.abs_diff(points[j].x) + 1) * (points[i].y.abs_diff(points[j].y) + 1);
            if area > max_area {
                max_area = area;
            }
        }
    }

    Some(max_area)
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
        assert_eq!(result, Some(50));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, None);
    }
}
